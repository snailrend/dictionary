package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.api.DictionaryItem;

import java.util.List;

public class MultipleDictionaryItemVo {
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName", valueDelimiter = ",")
    private String level;
    private List<DictionaryItem> levelName;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<DictionaryItem> getLevelName() {
        return levelName;
    }

    public void setLevelName(List<DictionaryItem> levelName) {
        this.levelName = levelName;
    }
}
