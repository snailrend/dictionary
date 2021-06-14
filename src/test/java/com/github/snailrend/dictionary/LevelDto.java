package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.api.DictionaryItem;

public class LevelDto implements DictionaryItem {
    private String value;
    private String name;

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
