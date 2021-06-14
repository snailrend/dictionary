package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class SingleNotSetterDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
    private String level;
    private String levelName;

    public SingleNotSetterDictionaryVo(String levelOwn, String level) {
        this.levelOwn = levelOwn;
        this.level = level;
    }

    public String getLevelOwn() {
        return levelOwn;
    }

    public String getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }
}
