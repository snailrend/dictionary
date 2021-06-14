package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.api.DictionaryItem;

public class SingleDictionaryItemVo {
    @Dictionary(dictionaryEnum = Level.class)
    private DictionaryItem levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
    private String level;
    private DictionaryItem levelName;

    public DictionaryItem getLevelOwn() {
        return levelOwn;
    }

    public void setLevelOwn(DictionaryItem levelOwn) {
        this.levelOwn = levelOwn;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DictionaryItem getLevelName() {
        return levelName;
    }

    public void setLevelName(DictionaryItem levelName) {
        this.levelName = levelName;
    }
}
