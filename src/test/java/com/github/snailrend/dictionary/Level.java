package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.api.DictionaryItem;

public enum Level implements DictionaryItem {
    one("1", "一"),
    two("2", "二"),
    three("3", "三"),
    four("4", "四"),
    five("5", "五");
    private String value;
    private String name;

    Level(String value, String name) {
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
