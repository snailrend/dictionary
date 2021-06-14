package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.api.DictionaryItem;

import java.util.*;

/**
 * 字典翻译类
 *
 * @author snailrend
 */
public class DictionaryConverter {
    /**
     * 字典数据 map，用 value 做 key
     */
    Map<String, DictionaryItem> dictionaryDataForValueKey;
    /**
     * 字典数据 map，用 name 做 key
     */
    Map<String, DictionaryItem> dictionaryDataForNameKey;

    protected DictionaryConverter(List<DictionaryItem> dictionaryItems) {
        this.dictionaryDataForValueKey = new HashMap<>();
        this.dictionaryDataForNameKey = new HashMap<>();
        putDictionaryData(dictionaryItems);
    }

    public void putDictionaryData(List<DictionaryItem> dictionaryItems) {
        for (DictionaryItem e : dictionaryItems) {
            dictionaryDataForValueKey.put(e.getValue(), e);
            dictionaryDataForNameKey.put(e.getName(), e);
        }
    }

    public static DictionaryConverter of(Class<? extends DictionaryItem> dictionaryClass) {
        DictionaryItem[] enumConstants = dictionaryClass.getEnumConstants();
        return of(Arrays.asList(enumConstants));
    }

    public static DictionaryConverter of(List<DictionaryItem> dictionaryData) {
        return new DictionaryConverter(dictionaryData);
    }

    /**
     * 通过 value 在指定的枚举类中找对应的枚举值
     *
     * @param value 字典值
     * @return 枚举值
     */
    public Optional<DictionaryItem> getByValue(String value) {
        if (dictionaryDataForValueKey.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(dictionaryDataForValueKey.get(value));
    }

    /**
     * 通过 name 在指定的枚举类中找对应的枚举值
     *
     * @param name 字典名称
     * @return 枚举值
     */
    public Optional<DictionaryItem> getByName(String name) {
        if (dictionaryDataForValueKey.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(dictionaryDataForNameKey.get(name));
    }

    /**
     * 通过 name 在指定的枚举类中找对应的字典名称
     *
     * @param value 字典值
     * @return 字典名称
     */
    public String getName(String value) {
        return getByValue(value).map(DictionaryItem::getName).orElse("");
    }

    /**
     * 通过 name 在指定的枚举类中找对应的字典名称,
     * 如果没找到，就返回 value
     *
     * @param value 字典值
     * @return 字典名称
     */
    public String getNameOrReturnValue(String value) {
        return getByValue(value).map(DictionaryItem::getName).orElse(value);
    }

    /**
     * 通过 name 在指定的枚举类中找对应的字典值
     *
     * @param name 字典名称
     * @return 字典值
     */
    public String getValue(String name) {
        return getByName(name).map(DictionaryItem::getValue).orElse("");
    }

    /**
     * 通过 name 在指定的枚举类中找对应的字典值
     * 如果没找到，就返回 name
     *
     * @param name 字典名称
     * @return 字典值
     */
    public String getValueOrReturnName(String name) {
        return getByName(name).map(DictionaryItem::getValue).orElse(name);
    }
}
