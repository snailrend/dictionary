package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class MultipleDiffDelimiterDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class, valueDelimiter = ",",nameDelimiter = "，")
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName", valueDelimiter = ",",nameDelimiter = "，")
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
