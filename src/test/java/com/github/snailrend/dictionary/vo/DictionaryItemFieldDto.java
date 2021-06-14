package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.SimpleDictionaryItem;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.api.DictionaryItem;

public class DictionaryItemFieldDto {
    @Dictionary(dictionaryEnum = Level.class,toField = "levelDict")
    private String level;
    private DictionaryItem levelDict;
    @Dictionary(dictionaryEnum = Level.class)
    private DictionaryItem levelDictOwn;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DictionaryItem getLevelDict() {
        return levelDict;
    }

    public void setLevelDict(DictionaryItem levelDict) {
        this.levelDict = levelDict;
    }

    public DictionaryItem getLevelDictOwn() {
        return levelDictOwn;
    }

    public void setLevelDictOwn(DictionaryItem levelDictOwn) {
        this.levelDictOwn = levelDictOwn;
    }
    public void setLevelDictOwn(String value) {
        this.levelDictOwn = new SimpleDictionaryItem(value);
    }
}
