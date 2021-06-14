package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.annotation.Dictionary;

import java.lang.reflect.Field;

/**
 * @author snailrend
 */
public class ConvertFieldSegment {
    private Object obj;
    private Dictionary annotation;
    private String rawValue;
    private Field toField;

    public ConvertFieldSegment(Object obj, Dictionary annotation, String rawValue, Field toField) {
        this.obj = obj;
        this.annotation = annotation;
        this.rawValue = rawValue;
        this.toField = toField;
    }

    public Object getObj() {
        return obj;
    }

    public Dictionary getAnnotation() {
        return annotation;
    }

    public String getRawValue() {
        return rawValue;
    }

    public Field getToField() {
        return toField;
    }
}
