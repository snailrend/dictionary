package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.api.DictionaryItem;

/**
 * @author snailrend
 */
public class SimpleDictionaryItem implements DictionaryItem {
    private String value;
    private String name;

    public SimpleDictionaryItem() {
    }

    public SimpleDictionaryItem(String value) {
        this.value = value;
    }

    public SimpleDictionaryItem(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
