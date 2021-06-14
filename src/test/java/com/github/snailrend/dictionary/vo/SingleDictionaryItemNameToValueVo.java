package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.api.DictionaryItem;
import com.github.snailrend.dictionary.enums.ConvertType;

public class SingleDictionaryItemNameToValueVo {
    @Dictionary(dictionaryEnum = Level.class,convertType = ConvertType.NAME_TO_VALUE)
    private DictionaryItem levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName",convertType = ConvertType.NAME_TO_VALUE)
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
