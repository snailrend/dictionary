package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.LevelDto;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class SingleErrorDictionaryVo {
    @Dictionary(dictionaryEnum = LevelDto.class)
    private String levelOwn;
    @Dictionary(dictionaryEnum = LevelDto.class,toField = "levelName")
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
