package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class SingleNotGetterDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
    private String level;
    private String levelName;

    public void setLevelOwn(String levelOwn) {
        this.levelOwn = levelOwn;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelOwn() {
        return levelOwn;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
