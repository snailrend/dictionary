package com.github.snailrend.dictionary.annotation;

import com.github.snailrend.dictionary.api.DictionaryItem;
import com.github.snailrend.dictionary.api.EmptyDictionary;
import com.github.snailrend.dictionary.enums.ConvertType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典注解，标记一个字段要被字典翻译
 *
 * @author snailrend
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dictionary {
    /**
     * 字典枚举class
     */
    Class<? extends DictionaryItem> dictionaryEnum() default EmptyDictionary.class;

    /**
     * 字典名称，用来获取数据
     */
    String dictionaryName() default "";

    /**
     * 转换类型，默认是字典值转换到字典名称
     */
    ConvertType convertType() default ConvertType.VALUE_TO_NAME;

    /**
     * 字典翻译后的字段
     * 为空时，将翻译后的值设置到注解所在的字段
     */
    String toField() default "";

    /**
     * 字典翻译前的字段，用来获取翻译前的值
     * 为空时，将从注解所在的字段取值
     */
    String fromField() default "";

    /**
     * 字典值分隔符
     * 有值时，按照分隔符切割，将切割后的每个元素视为一个字典值
     */
    String valueDelimiter() default "";

    /**
     * 字典名称分隔符
     * 有值时，按照分隔符切割，将切割后的每个元素视为一个字典值
     */
    String nameDelimiter() default "";
}
