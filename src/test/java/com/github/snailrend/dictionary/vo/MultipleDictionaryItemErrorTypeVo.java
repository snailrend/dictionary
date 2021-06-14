package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.annotation.Dictionary;

import java.util.List;

public class MultipleDictionaryItemErrorTypeVo {
    @Dictionary(dictionaryName = "a",toField = "levelName", valueDelimiter = ",")
    private String level;
    private List<String> levelName;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getLevelName() {
        return levelName;
    }

    public void setLevelName(List<String> levelName) {
        this.levelName = levelName;
    }
}
