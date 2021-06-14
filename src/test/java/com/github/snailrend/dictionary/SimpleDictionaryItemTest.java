package com.github.snailrend.dictionary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleDictionaryItemTest {

    @Test
    public void testConstrcat(){
        SimpleDictionaryItem simpleDictionaryItem = new SimpleDictionaryItem();
        Assertions.assertNull(simpleDictionaryItem.getValue());
        Assertions.assertNull(simpleDictionaryItem.getName());
    }

    @Test
    public void testConstrcat1(){
        SimpleDictionaryItem simpleDictionaryItem = new SimpleDictionaryItem("1");
        Assertions.assertEquals("1",simpleDictionaryItem.getValue());
        Assertions.assertNull(simpleDictionaryItem.getName());
    }

    @Test
    public void testConstrcat2(){
        SimpleDictionaryItem simpleDictionaryItem = new SimpleDictionaryItem("1","一");
        Assertions.assertEquals("1",simpleDictionaryItem.getValue());
        Assertions.assertEquals("一",simpleDictionaryItem.getName());
    }

    @Test
    public void testConstrcat3(){
        SimpleDictionaryItem simpleDictionaryItem = new SimpleDictionaryItem();
        simpleDictionaryItem.setValue("1");
        Assertions.assertEquals("1",simpleDictionaryItem.getValue());
        Assertions.assertNull(simpleDictionaryItem.getName());
    }
    @Test
    public void testConstrcat4(){
        SimpleDictionaryItem simpleDictionaryItem = new SimpleDictionaryItem();
        simpleDictionaryItem.setName("1");
        Assertions.assertNull(simpleDictionaryItem.getValue());
        Assertions.assertEquals("1",simpleDictionaryItem.getName());
    }
}
