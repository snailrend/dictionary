package com.github.snailrend.dictionary.vo;

import com.github.snailrend.dictionary.Level;
import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.annotation.DictionaryConvertible;

@DictionaryConvertible
public class DeepDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class)
    private String level;
    private DeepDictionaryVo dictionary1;

    public DeepDictionaryVo() {
    }

    public DeepDictionaryVo(String level) {
        this.level = level;
    }

    public DeepDictionaryVo(String level, DeepDictionaryVo dictionary1) {
        this.level = level;
        this.dictionary1 = dictionary1;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DeepDictionaryVo getDictionary1() {
        return dictionary1;
    }

    public void setDictionary1(DeepDictionaryVo dictionary1) {
        this.dictionary1 = dictionary1;
    }
}
