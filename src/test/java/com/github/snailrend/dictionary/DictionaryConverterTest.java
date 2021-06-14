package com.github.snailrend.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryConverterTest {

    private DictionaryConverter dictionaryConverterUnderTest;

    @BeforeEach
    void setUp() {
        dictionaryConverterUnderTest = DictionaryConverter.of(Level.class);
    }

    @Test
    void testGetByValue() {

        assertEquals(Optional.empty(), dictionaryConverterUnderTest.getByValue("value"));
        assertEquals(Optional.of(Level.one), dictionaryConverterUnderTest.getByValue("1"));
    }

    @Test
    void testGetByName() {
        assertEquals(Optional.empty(), dictionaryConverterUnderTest.getByName("name"));
        assertEquals(Optional.of(Level.one), dictionaryConverterUnderTest.getByName("一"));
    }

    @Test
    void testGetName() {
        assertEquals("", dictionaryConverterUnderTest.getName("value"));
        assertEquals("一", dictionaryConverterUnderTest.getName("1"));
    }

    @Test
    void testGetNameOrReturnValue() {
        assertEquals("value", dictionaryConverterUnderTest.getNameOrReturnValue("value"));
        assertEquals("一", dictionaryConverterUnderTest.getNameOrReturnValue("1"));
    }

    @Test
    void testGetValue() {
        assertEquals("", dictionaryConverterUnderTest.getValue("name"));
        assertEquals("1", dictionaryConverterUnderTest.getValue("一"));
    }

    @Test
    void testGetValueOrReturnName() {
        assertEquals("name", dictionaryConverterUnderTest.getValueOrReturnName("name"));
        assertEquals("1", dictionaryConverterUnderTest.getValueOrReturnName("一"));
    }

    @Test
    void testOf() {
        DictionaryConverter of = DictionaryConverter.of(Level.class);
        assertNotNull(of.dictionaryDataForValueKey);
    }
    @Test
    void testOf1() {
        assertThrows(NullPointerException.class,()->{
            DictionaryConverter.of(LevelDto.class);
        });
    }
    @Test
    void testOf2() {
        DictionaryConverter of = DictionaryConverter.of(new ArrayList<>());
        assertTrue(of.dictionaryDataForValueKey.isEmpty());
    }
}
