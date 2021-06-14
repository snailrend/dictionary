package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.annotation.Dictionary;

public class SingleErrorDictionaryNameVo {
    @Dictionary(dictionaryName = "dictionary0")
    private String levelOwn;
    @Dictionary(dictionaryName = "dictionary0",toField = "levelName")
    private String level;
    private String levelName;

    public String getLevelOwn() {
        return levelOwn;
    }

    public void setLevelOwn(String levelOwn) {
        this.levelOwn = levelOwn;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
