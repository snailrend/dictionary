package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class ChildExtDictionaryVo extends SingleDictionaryVo {

    @Dictionary(dictionaryEnum = Level.class,fromField= "level")
    private String levelName1;

    public String getLevelName1() {
        return levelName1;
    }
}
