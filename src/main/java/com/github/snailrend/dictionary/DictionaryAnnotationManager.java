package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.annotation.Dictionary;
import com.github.snailrend.dictionary.api.DictionaryItem;
import com.github.snailrend.dictionary.enums.ConvertType;

/**
 * @author snailrend
 */
public class DictionaryAnnotationManager {
    private Dictionary annotaion;

    public DictionaryAnnotationManager(Dictionary annotaion) {
        this.annotaion = annotaion;
    }

    public Dictionary annotaion() {
        return annotaion;
    }

    public boolean singleConvert() {
        if (annotaion.convertType().equals(ConvertType.VALUE_TO_NAME)) {
            return annotaion.valueDelimiter() == null || annotaion.valueDelimiter().isEmpty();
        }else {
            return annotaion.nameDelimiter() == null || annotaion.nameDelimiter().isEmpty();
        }
    }

    public String rawValueDelimiter() {
        if (annotaion.convertType().equals(ConvertType.VALUE_TO_NAME)) {
            return annotaion.valueDelimiter();
        }else {
            return annotaion.nameDelimiter();
        }
    }

    public String readyValueDelimiter() {
        if (isBlank(annotaion.nameDelimiter())) {
            return annotaion.valueDelimiter();
        }
        if (isBlank(annotaion.valueDelimiter())) {
            return annotaion.nameDelimiter();
        }
        if (annotaion.convertType().equals(ConvertType.VALUE_TO_NAME)) {
            return annotaion.nameDelimiter();
        }else {
            return annotaion.valueDelimiter();
        }
    }
    private boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    public String getReadyValue(DictionaryItem dictionaryItem) {
        String value = dictionaryItem.getValue();
        String name = dictionaryItem.getName();
        if (annotaion.convertType().equals(ConvertType.NAME_TO_VALUE)) {
            return value == null ? name : value;
        } else {
            return name == null ? value : name;
        }
    }

    public DictionaryItem getConvertFailDictionaryItem(String rawValue) {
        if (annotaion.convertType().equals(ConvertType.NAME_TO_VALUE)) {
            return new SimpleDictionaryItem(null, rawValue);
        } else {
            return new SimpleDictionaryItem(rawValue);
        }
    }
}
