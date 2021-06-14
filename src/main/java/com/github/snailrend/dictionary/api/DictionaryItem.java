package com.github.snailrend.dictionary.api;


/**
 * 包含 value 与 name 的接口
 *
 * @author snailrend
 */
public interface DictionaryItem extends ValueAble {
    /**
     * 获取 name
     *
     * @return name
     */
    String getName();
}
