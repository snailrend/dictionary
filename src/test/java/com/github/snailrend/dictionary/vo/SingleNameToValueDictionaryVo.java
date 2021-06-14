package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.enums.ConvertType;

public class SingleNameToValueDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class,convertType = ConvertType.NAME_TO_VALUE)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName",convertType = ConvertType.NAME_TO_VALUE)
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
