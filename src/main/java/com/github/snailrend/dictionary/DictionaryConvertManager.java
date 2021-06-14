package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.annotation.DictionaryConvertible;
import com.github.snailrend.dictionary.api.DictionaryDataLoader;
import com.github.snailrend.dictionary.api.DictionaryItem;
import com.github.snailrend.dictionary.api.EmptyDictionary;
import com.github.snailrend.dictionary.enums.ConvertType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典翻译管理类
 *
 * @author snailrend
 */
public class DictionaryConvertManager {
    /**
     * 使用 ${@link Dictionary#dictionaryName()} 时，将从此接口获取字典数据
     * 请自行捕获内部的异常，否则会导致翻译时抛出异常
     */
    protected DictionaryDataLoader dictionaryDataLoader;
    /**
     * 一个线程级的字段转换器缓存，仅保存数据来源从${@link this#dictionaryDataLoader} 获取的数据
     */
    protected ThreadLocal<Map<String, DictionaryConverter>> threadCache = ThreadLocal.withInitial(HashMap::new);
    /**
     * 一个简单的字典转换器缓存，仅保存数据来源为枚举的转换器
     * 若是从其他来源获取的字典数据，可由 ${@link this#dictionaryDataLoader} 内部做缓存
     * <p>
     * 因为常规情况下枚举的数据在一次启动中不会发生变更，直接用 map 就行
     */
    protected Map<Class<? extends DictionaryItem>, DictionaryConverter> simpleEnumCache = new HashMap<>();

    public DictionaryConvertManager() {
    }

    public DictionaryConvertManager(DictionaryDataLoader dictionaryDataLoader) {
        this.dictionaryDataLoader = dictionaryDataLoader;
    }

    /**
     * 翻译一个对象中 带有 ${@link Dictionary} 的字段
     * 若是字段类型有标记 ${@link DictionaryConvertible}，也会递归地翻译字段的值
     *
     * @param obj 需要翻译字段的对象
     * @return 翻译后的对象（跟入参是同一个对象）
     */
    public <T> T convert(T obj) {
        List<ConvertFieldSegment> convertFieldSegments = collectConvertFieldSegments(obj, new HashSet<>(), new ArrayList<>());
        convertFieldSegmentApply(convertFieldSegments);
        threadCache.get().clear();
        return obj;
    }

    /**
     * 翻译列表中所有对象中 带有 ${@link Dictionary} 的字段
     * 若是字段类型有标记 ${@link DictionaryConvertible}，也会递归地翻译字段的值
     *
     * @param objs 需要翻译字段的对象列表
     */
    public <T> void convertList(List<T> objs) {
        Set<Integer> ignoreHashSet = new HashSet<>();
        ArrayList<ConvertFieldSegment> convertFieldSegmentList = new ArrayList<>();
        for (T obj : objs) {
            collectConvertFieldSegments(obj, ignoreHashSet, convertFieldSegmentList);
        }
        convertFieldSegmentApply(convertFieldSegmentList);

        threadCache.get().clear();
    }

    /**
     * 将列表中的字段都执行翻译
     *
     * @param convertFieldSegmentList 字段翻译片段列表
     */
    private void convertFieldSegmentApply(List<ConvertFieldSegment> convertFieldSegmentList) {
        Map<String, Map<ConvertType, List<String>>> rawValueForNeedDictionaryData = getRawValueForNeedDictionaryData(convertFieldSegmentList);

        //按照需要翻译的value或者name，先加载数据到线程级的缓存中,后续翻译时，就不再重复请求了
        rawValueForNeedDictionaryData.forEach((dictionaryName, convertTypeListMap) -> {
            convertTypeListMap.computeIfPresent(ConvertType.VALUE_TO_NAME, (convertType, rawValues) -> {
                putThreadCacheForLoadParams(dictionaryName, buildValuesParams(dictionaryName, rawValues));
                return rawValues;
            });
            convertTypeListMap.computeIfPresent(ConvertType.NAME_TO_VALUE, (convertType, rawValues) -> {
                putThreadCacheForLoadParams(dictionaryName, buildNamesParams(dictionaryName, rawValues));
                return rawValues;
            });
        });

        //逐个字段进行翻译
        for (ConvertFieldSegment convertFieldSegment : convertFieldSegmentList) {
            Object readyValue = getReadyValue(
                    convertFieldSegment.getAnnotation(),
                    convertFieldSegment.getRawValue(),
                    convertFieldSegment.getToField().getType());
            setFieldValue(convertFieldSegment.getObj(), convertFieldSegment.getToField(), readyValue);
        }
    }

    /**
     * 构建一个按照字典项名称加载字典数据的参数对象
     *
     * @param dictionaryName 字典名
     * @param names          字典项名称
     * @return 字典加载参数对象
     */
    private DictionaryDataLoader.LoadParams buildNamesParams(String dictionaryName, List<String> names) {
        removeIfExistNames(names, dictionaryName);
        DictionaryDataLoader.LoadParams loadParams = new DictionaryDataLoader.LoadParams();
        loadParams.setNames(new HashSet<>(names));
        return loadParams;
    }

    /**
     * 构建一个按照字典项值加载字典数据的参数对象
     *
     * @param dictionaryName 字典名
     * @param values         字典项名称
     * @return 字典加载参数对象
     */
    private DictionaryDataLoader.LoadParams buildValuesParams(String dictionaryName, List<String> values) {
        removeIfExistValues(values, dictionaryName);
        DictionaryDataLoader.LoadParams loadParams = new DictionaryDataLoader.LoadParams();
        loadParams.setValues(new HashSet<>(values));
        return loadParams;
    }

    /**
     * 根据字典加载参数，将加载到的数据放入缓存的字典转换器中
     *
     * @param dictionaryName 字典名
     * @param loadParams     字典加载参数对象
     */
    private void putThreadCacheForLoadParams(String dictionaryName, DictionaryDataLoader.LoadParams loadParams) {
        ;
        List<DictionaryItem> dictionaryItems = loadDictionaryItems(dictionaryName, loadParams);

        threadCache.get().put(dictionaryName, putDictionaryData(dictionaryName, dictionaryItems));
    }

    /**
     * 检查缓存的字典转换器中是否有 values 中需要的数据，如果有，就将 values 中对应的项删除
     *
     * @param values         字典项值的集合
     * @param dictionaryName 字典名
     */
    private void removeIfExistValues(List<String> values, String dictionaryName) {
        Optional<DictionaryConverter> dictionaryConverterOptional =
                Optional.ofNullable(threadCache.get().get(dictionaryName));

        dictionaryConverterOptional.ifPresent(dictionaryConverter ->
                values.removeAll(dictionaryConverter.dictionaryDataForValueKey.keySet()));
    }

    /**
     * 检查缓存的字典转换器中是否有 names 中需要的数据，如果有，就将 names 中对应的项删除
     *
     * @param names          字典项值的集合
     * @param dictionaryName 字典名
     */
    private void removeIfExistNames(List<String> names, String dictionaryName) {
        Optional<DictionaryConverter> dictionaryConverterOptional =
                Optional.ofNullable(threadCache.get().get(dictionaryName));

        dictionaryConverterOptional.ifPresent(dictionaryConverter ->
                names.removeAll(dictionaryConverter.dictionaryDataForNameKey.keySet()));
    }


    /**
     * 将字典数据放入缓存的字典转换器中
     *
     * @param dictionaryName  字典名
     * @param dictionaryItems 字典加载参数对象
     */
    private DictionaryConverter putDictionaryData(String dictionaryName, List<DictionaryItem> dictionaryItems) {
        return Optional.ofNullable(threadCache.get().get(dictionaryName)).map(dictionaryConverter -> {
            dictionaryConverter.putDictionaryData(dictionaryItems);
            return dictionaryConverter;
        }).orElseGet(() -> DictionaryConverter.of(dictionaryItems));
    }

    /**
     * 根据字典名与字典加载参数，加载字典数据
     *
     * @param dictionaryName 字典名
     * @param loadParams     字典加载参数对象
     * @return 字典数据
     */
    private List<DictionaryItem> loadDictionaryItems(String dictionaryName, DictionaryDataLoader.LoadParams loadParams) {
        return Optional.ofNullable(dictionaryDataLoader)
                .map(dataLoader -> dataLoader.load(dictionaryName, loadParams))
                .orElse(new ArrayList<>());
    }

    /**
     * 从所有需要翻译的字段集合中，按照字典名以及翻译的类型进行二级分组
     *
     * @param convertFieldSegmentList 需要翻译的字段列表
     * @return 按照字典名以及翻译的类型进行分组后的二级分组 map
     */
    private Map<String, Map<ConvertType, List<String>>> getRawValueForNeedDictionaryData(List<ConvertFieldSegment> convertFieldSegmentList) {
        return convertFieldSegmentList.stream()
                .filter(convertFieldSegment -> convertFieldSegment.getAnnotation().dictionaryEnum().equals(EmptyDictionary.class))
                .filter(convertFieldSegment -> !Objects.equals(convertFieldSegment.getAnnotation().dictionaryName(), ""))
                .collect(Collectors.groupingBy(convertFieldSegment -> convertFieldSegment.getAnnotation().dictionaryName(),
                        Collectors.groupingBy(convertFieldSegment1 -> convertFieldSegment1.getAnnotation().convertType(),
                                Collectors.mapping(ConvertFieldSegment::getRawValue, Collectors.toList()))));
    }

    /**
     * 收集一个对象中 带有 ${@link Dictionary} 的字段
     * 若是字段类型有标记 ${@link DictionaryConvertible}，也会递归地收集字段的值中的字段
     *
     * @param obj                     需要翻译字段的对象
     * @param ignoreHashSet           需要忽略掉的对象 hash 集合
     * @param convertFieldSegmentList 已有的需要翻译的字段
     * @return 翻译后的对象（跟入参是同一个对象）
     */
    protected <T> List<ConvertFieldSegment> collectConvertFieldSegments(T obj, Set<Integer> ignoreHashSet, List<ConvertFieldSegment> convertFieldSegmentList) {
        if (obj == null) {
            return convertFieldSegmentList;
        }
        //获取对象原始的哈希码
        int objHash = System.identityHashCode(obj);
        if (ignoreHashSet.contains(objHash)) {
            return convertFieldSegmentList;
        }
        List<Field> fields = getAllField(obj.getClass());

        List<Field> dictionaryFields = new ArrayList<>();
        Map<String, Field> fieldMap = new HashMap<>();
        List<Object> dictionaryFieldValues = new ArrayList<>();

        //从全部的字段中，区分出 需要翻译的字段、需要翻译的字段值
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);

            if (field.getAnnotation(Dictionary.class) != null) {
                dictionaryFields.add(field);
            }
            if (field.getType().getAnnotation(DictionaryConvertible.class) != null) {
                Object fieldValue = getFieldValue(obj, field);
                if (fieldValue != obj) {
                    dictionaryFieldValues.add(fieldValue);
                }
            }
        }
        //对需要翻译的字段进行翻译
        for (Field field : dictionaryFields) {
            buildConvertFieldSegment(obj, fieldMap, field)
                    .ifPresent(convertFieldSegmentList::add);
        }
        ignoreHashSet.add(objHash);
        //对需要翻译的字段值进行翻译
        for (Object dictionaryFieldValue : dictionaryFieldValues) {
            collectConvertFieldSegments(dictionaryFieldValue, ignoreHashSet, convertFieldSegmentList);
        }
        return convertFieldSegmentList;
    }

    /**
     * 获取类型的所有字段，包含父类中的字段
     *
     * @param clazz 类型
     * @return 字段列表
     */
    protected <T> List<Field> getAllField(Class<?> clazz) {
        ArrayList<Field> fields = getFields(clazz);
        while (!(clazz = clazz.getSuperclass()).equals(Object.class)) {
            fields.addAll(getFields(clazz));
        }
        return fields;
    }

    /**
     * 获取一个类型的所有字段，不包含父类的字段
     *
     * @param clazz 类型
     * @return 字段列表
     */
    protected ArrayList<Field> getFields(Class<?> clazz) {
        return new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
    }


    /**
     * 翻译一个字段
     *
     * @param obj      字段所属对象
     * @param fieldMap 字段 map 索引
     * @param field    组要翻译的字段
     * @return
     */
    protected <T> Optional<ConvertFieldSegment> buildConvertFieldSegment(T obj, Map<String, Field> fieldMap, Field field) {
        Dictionary dictionaryAnnotation = field.getAnnotation(Dictionary.class);

        Field fromField = fieldMap.getOrDefault(dictionaryAnnotation.fromField(), field);
        String rawValue = getFieldValueStr(obj, fromField);
        if (rawValue == null || "".equals(rawValue)) {
            return Optional.empty();
        }

        Field toField = fieldMap.getOrDefault(dictionaryAnnotation.toField(), field);
        return Optional.of(new ConvertFieldSegment(obj, dictionaryAnnotation, rawValue, toField));
    }

    /**
     * 根据 toField 的类型，获取 rawValue 对应的翻译完后的的 readyValue，
     *
     * @param dictionaryAnnotation 字典注解对象
     * @param rawValue             原始值
     * @param toFieldType          toField 字段类型
     * @return 翻译完后的值
     */
    private Object getReadyValue(Dictionary dictionaryAnnotation, String rawValue, Class<?> toFieldType) {
        DictionaryAnnotationManager annotationManager = new DictionaryAnnotationManager(dictionaryAnnotation);
        Object readyValue = null;

        if (annotationManager.singleConvert()) {
            Optional<DictionaryItem> singleItemOptional = convertSingleRawValue(rawValue, annotationManager.annotaion());

            if (String.class.equals(toFieldType)) {
                readyValue = singleItemOptional.map(annotationManager::getReadyValue).orElse(rawValue);
            } else if (DictionaryItem.class.equals(toFieldType)) {
                readyValue = singleItemOptional
                        .orElseGet(() -> annotationManager.getConvertFailDictionaryItem(rawValue));
            }
        } else {
            List<DictionaryItem> dictionaryItems = convertMultipleRawValue(rawValue, annotationManager);
            if (String.class.equals(toFieldType)) {
                readyValue = dictionaryItems.stream()
                        .map(annotationManager::getReadyValue)
                        .collect(Collectors.joining(annotationManager.readyValueDelimiter()));
            } else if (List.class.equals(toFieldType)) {
                readyValue = dictionaryItems;
            } else if (DictionaryItem.class.equals(toFieldType)) {
                readyValue = dictionaryItems.get(0);
            }
        }
        return readyValue;
    }

    /**
     * 翻译单个值，根据字段上的注解标记
     *
     * @param rawValue             字段值
     * @param dictionaryAnnotation 字典翻译注解
     * @return 翻译后的值
     */
    protected Optional<DictionaryItem> convertSingleRawValue(String rawValue, Dictionary dictionaryAnnotation) {
        DictionaryConverter converter = getConverter(dictionaryAnnotation);
        if (dictionaryAnnotation.convertType().equals(ConvertType.VALUE_TO_NAME)) {
            return converter.getByValue(rawValue);
        } else {
            return converter.getByName(rawValue);
        }
    }

    /**
     * 获取一个字典翻译实例，根据给定的字典翻译注解
     *
     * @param dictionaryAnnotation 字典翻译注解
     * @return 字典翻译实例
     */
    protected DictionaryConverter getConverter(Dictionary dictionaryAnnotation) {
        Class<? extends DictionaryItem> dictionaryClass = dictionaryAnnotation.dictionaryEnum();
        if (dictionaryClass.equals(EmptyDictionary.class)) {
            return getDictionaryConverter(dictionaryAnnotation.dictionaryName());
        }
        return getDictionaryConverter(dictionaryClass);
    }

    /**
     * 根据字典名称获取一个字典翻译实例（通过 ${@link this#dictionaryDataLoader} 获取数据）
     *
     * @param dictionaryName 字典名称
     * @return 字典翻译实例
     */
    public DictionaryConverter getDictionaryConverter(String dictionaryName) {
        Map<String, DictionaryConverter> converterMap = threadCache.get();
        if (converterMap.containsKey(dictionaryName)) {
            return converterMap.get(dictionaryName);
        }
        DictionaryConverter dictionaryConverter = DictionaryConverter.of(
                Optional.ofNullable(dictionaryDataLoader)
                        .map(dataLoader -> dataLoader.load(dictionaryName, new DictionaryDataLoader.LoadParams()))
                        .orElse(new ArrayList<>()));
        converterMap.put(dictionaryName, dictionaryConverter);
        return dictionaryConverter;
    }

    /**
     * 根据字典枚举类获取一个字典翻译实例
     *
     * @param dictionaryClass 字典枚举类
     * @return 字典翻译实例
     */
    public DictionaryConverter getDictionaryConverter(Class<? extends DictionaryItem> dictionaryClass) {
        return Optional.ofNullable(simpleEnumCache.get(dictionaryClass))
                .orElseGet(() -> {
                    DictionaryConverter dictionaryConverter = DictionaryConverter.of(dictionaryClass);
                    simpleEnumCache.put(dictionaryClass, dictionaryConverter);
                    return dictionaryConverter;
                });
    }

    /**
     * 根据字典翻译注解，翻译多个按照某种分隔符分割的字典值，分隔符按照 ${@link Dictionary#valueDelimiter()} 指定的分隔符分割
     * 翻译后的字典值按照 ${@link Dictionary#nameDelimiter()} 分割
     *
     * @param rawValues            字段值
     * @param dictionaryAnnotation 字典翻译注解
     * @return 翻译后的值
     */
    protected List<DictionaryItem> convertMultipleRawValue(String rawValues, DictionaryAnnotationManager dictionaryAnnotation) {
        String rawValueDelimiter = dictionaryAnnotation.rawValueDelimiter();
        return Arrays.stream(rawValues.split(rawValueDelimiter))
                .map(value -> convertSingleRawValue(value, dictionaryAnnotation.annotaion())
                        .orElseGet(() -> dictionaryAnnotation.getConvertFailDictionaryItem(value)))
                .collect(Collectors.toList());
    }

    /**
     * 为一个字段设置值，优先使用 setter 方法，若是找不到才直接设置值
     *
     * @param obj   字段所属对象
     * @param field 字段对象
     * @param value 要设置的字段值
     */
    protected <T> void setFieldValue(T obj, Field field, Object value) {
        Optional<Method> setterMethod = getSetterMethod(obj, field);
        try {
            if (setterMethod.isPresent()) {
                setterMethod.get().invoke(obj, value);
            } else {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("设置字段值失败", e);
        }
    }

    /**
     * 获取一个字段转换为 String 的值
     *
     * @param obj   字段所属对象
     * @param field 字段对象
     * @return 字段值
     */
    protected <T> String getFieldValueStr(T obj, Field field) {
        return Optional.ofNullable(getFieldValue(obj, field)).map(Object::toString).orElse(null);
    }

    /**
     * 获取一个字段的值对象
     *
     * @param obj   字段所属对象
     * @param field 字段对象
     * @return 字段值对象
     */
    protected <T> Object getFieldValue(T obj, Field field) {
        Optional<Method> getterMethod = getGetterMethod(obj, field);
        try {
            if (getterMethod.isPresent()) {
                return unboxingIfTypeOfDictionaryItem(getterMethod.get().invoke(obj), field);
            } else {
                field.setAccessible(true);
                return unboxingIfTypeOfDictionaryItem(field.get(obj), field);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
    }

    /**
     * 如果对象类型是 ${@link DictionaryItem} 就自动拆箱获取value
     *
     * @param value 对象实例
     * @param field
     * @return 原始的 value 或者 ${@link DictionaryItem#getValue()} 的值
     */
    private Object unboxingIfTypeOfDictionaryItem(Object value, Field field) {
        if (value instanceof DictionaryItem) {
            Dictionary annotation = field.getAnnotation(Dictionary.class);
            if (annotation.convertType().equals(ConvertType.NAME_TO_VALUE)) {
                return ((DictionaryItem) value).getName();
            } else {
                return ((DictionaryItem) value).getValue();
            }
        }
        return value;
    }

    /**
     * 获取一个字段的 setter 方法对象
     *
     * @param obj   字段所属对象
     * @param field 字段对象
     * @return 字段的 setter 方法对象
     */
    protected <T> Optional<Method> getSetterMethod(T obj, Field field) {
        String setterFucName = getSetterFucName(field.getName());
        try {
            Method method = obj.getClass().getMethod(setterFucName, field.getType());
            method.setAccessible(true);
            return Optional.of(method);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }


    /**
     * 获取一个字段的 getter 方法对象
     *
     * @param obj   字段所属对象
     * @param field 字段对象
     * @return 字段的 getter 方法对象
     */
    protected <T> Optional<Method> getGetterMethod(T obj, Field field) {
        String getterFucName = getGetterFucName(field.getName());
        try {
            Method method = obj.getClass().getMethod(getterFucName);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    /**
     * 获取一个字段的 getter 方法名称
     *
     * @param name 字段名
     * @return getter 方法名称
     */
    protected String getGetterFucName(String name) {
        return "get" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    /**
     * 获取一个字段的 setter 方法名称
     *
     * @param name 字段名
     * @return setter 方法名称
     */
    protected String getSetterFucName(String name) {
        return "set" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }
}
