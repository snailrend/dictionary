package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;

public class ChildDictionaryVo extends SingleDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class)
    private String childLevelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
    private String childLevel;

    public ChildDictionaryVo() {
    }

    public String getChildLevelOwn() {
        return childLevelOwn;
    }

    public void setChildLevelOwn(String childLevelOwn) {
        this.childLevelOwn = childLevelOwn;
    }

    public String getChildLevel() {
        return childLevel;
    }

    public void setChildLevel(String childLevel) {
        this.childLevel = childLevel;
    }
}
