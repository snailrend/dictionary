package com.github.snailrend.dictionary;

import com.github.snailrend.dictionary.api.DictionaryDataLoader;
import com.github.snailrend.dictionary.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

class DictionaryConvertManagerTest {

    private DictionaryConvertManager dictionaryConvertManagerUnderTest = new DictionaryConvertManager();

    @Test
    void testNewDictionaryConvertManager(){

        AtomicReference<String> queryDictionaryName = new AtomicReference<>();
        DictionaryDataLoader stringListFunction = (dictionaryName,loaderParams) -> {
            queryDictionaryName.set(dictionaryName);
            if (loaderParams.isEmpty()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(9);
        };

        DictionaryConvertManager dictionaryConvertManager = new DictionaryConvertManager(stringListFunction);
        Assertions.assertNull(queryDictionaryName.get());
        dictionaryConvertManager.getDictionaryConverter("aa");
        Assertions.assertEquals("aa",queryDictionaryName.get());
        dictionaryConvertManager.getDictionaryConverter("bb");
        Assertions.assertEquals("bb",queryDictionaryName.get());
    }

    @Test
    void testConvert0() {
        SingleErrorDictionaryVo singleDictionaryVo = new SingleErrorDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getValue());
        singleDictionaryVo.setLevel(Level.two.getValue());
        Assertions.assertThrows(NullPointerException.class,()->{
            dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        });
    }

    @Test
    void testConvert01() {
        SingleErrorNameToValueDictionaryVo singleDictionaryVo = new SingleErrorNameToValueDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getName());
        singleDictionaryVo.setLevel(Level.two.getName());
        Assertions.assertThrows(NullPointerException.class,()->{
            dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        });
    }
    @Test
    void testConvert05() {
        SingleErrorDictionaryNameVo singleDictionaryVo = new SingleErrorDictionaryNameVo();
        singleDictionaryVo.setLevelOwn(Level.one.getValue());
        singleDictionaryVo.setLevel(Level.two.getValue());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevel());

        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert06() {
        SingleErrorNameToValueDictionaryNameVo singleDictionaryVo = new SingleErrorNameToValueDictionaryNameVo();
        singleDictionaryVo.setLevelOwn(Level.one.getName());
        singleDictionaryVo.setLevel(Level.two.getName());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("二", singleDictionaryVo.getLevel());

        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("二", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert02() {
        SingleErrorDictionaryVo singleDictionaryVo = new SingleErrorDictionaryVo();
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertNull(singleDictionaryVo.getLevelOwn());
        Assertions.assertNull(singleDictionaryVo.getLevelName());
        Assertions.assertNull(singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert03() {
        SingleSetterExceptionDictionaryVo singleDictionaryVo = new SingleSetterExceptionDictionaryVo();
        singleDictionaryVo.setLevel("1");

        Assertions.assertThrows(RuntimeException.class, () -> {
            dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        });
    }

    @Test
    void testConvert04() {
        SingleGetterExceptionDictionaryVo singleDictionaryVo = new SingleGetterExceptionDictionaryVo();
        singleDictionaryVo.setLevelOwn("1");
        Assertions.assertThrows(RuntimeException.class, () -> {
            dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        });
    }

    @Test
    void testConvert1() {
        SingleDictionaryVo singleDictionaryVo = new SingleDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getValue());
        singleDictionaryVo.setLevel(Level.two.getValue());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert10() {
        SingleDictionaryVo singleDictionaryVo = new SingleDictionaryVo();
        singleDictionaryVo.setLevelOwn("6");
        singleDictionaryVo.setLevel("7");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("6", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("7", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("7", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert11() {
        SingleNameToValueDictionaryVo singleDictionaryVo = new SingleNameToValueDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getName());
        singleDictionaryVo.setLevel(Level.two.getName());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("二", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert12() {
        SingleNameToValueDictionaryVo singleDictionaryVo = new SingleNameToValueDictionaryVo();
        singleDictionaryVo.setLevelOwn("6");
        singleDictionaryVo.setLevel("7");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("6", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("7", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("7", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert13() {
        SingleNotSetterDictionaryVo singleDictionaryVo = new SingleNotSetterDictionaryVo(Level.one.getValue(), Level.two.getValue());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert14() {
        SingleNotGetterDictionaryVo singleDictionaryVo = new SingleNotGetterDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getValue());
        singleDictionaryVo.setLevel(Level.two.getValue());
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
    }
    @Test
    void testConvert15() {
        SingleDictionaryItemVo singleDictionaryVo = new SingleDictionaryItemVo();
        singleDictionaryVo.setLevelOwn(new SimpleDictionaryItem("1"));
        singleDictionaryVo.setLevel("2");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn().getValue());
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn().getName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName().getValue());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName().getName());
    }
    @Test
    void testConvert16() {
        SingleDictionaryItemVo singleDictionaryVo = new SingleDictionaryItemVo();
        singleDictionaryVo.setLevelOwn(new SimpleDictionaryItem("1"));
        singleDictionaryVo.setLevel("8");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn().getValue());
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn().getName());
        Assertions.assertEquals("8", singleDictionaryVo.getLevelName().getValue());
        Assertions.assertNull(singleDictionaryVo.getLevelName().getName());
    }
    @Test
    void testConvert17() {
        SingleDictionaryItemNameToValueVo singleDictionaryVo = new SingleDictionaryItemNameToValueVo();
        singleDictionaryVo.setLevelOwn(new SimpleDictionaryItem("","一"));
        singleDictionaryVo.setLevel("二");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1", singleDictionaryVo.getLevelOwn().getValue());
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn().getName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName().getValue());
        Assertions.assertEquals("二",singleDictionaryVo.getLevelName().getName());
    }
    @Test
    void testConvert18() {
        SingleDictionaryItemNameToValueVo singleDictionaryVo = new SingleDictionaryItemNameToValueVo();
        singleDictionaryVo.setLevelOwn(new SimpleDictionaryItem("","六"));
        singleDictionaryVo.setLevel("二");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertNull(singleDictionaryVo.getLevelOwn().getValue());
        Assertions.assertEquals("六", singleDictionaryVo.getLevelOwn().getName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevelName().getValue());
        Assertions.assertEquals("二",singleDictionaryVo.getLevelName().getName());
    }

    @Test
    void testConvert2() {
        MultipleDictionaryVo singleDictionaryVo = new MultipleDictionaryVo();
        singleDictionaryVo.setLevelOwn("1,2");
        singleDictionaryVo.setLevel("3,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一,二", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("三,四", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("3,4", singleDictionaryVo.getLevel());
    }
    @Test
    void testConvert20() {
        MultipleDictionaryVo singleDictionaryVo = new MultipleDictionaryVo();
        singleDictionaryVo.setLevelOwn("1,6,2");
        singleDictionaryVo.setLevel("3,8,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一,6,二", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("三,8,四", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("3,8,4", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert21() {
        MultipleNameToValueDictionaryVo singleDictionaryVo = new MultipleNameToValueDictionaryVo();
        singleDictionaryVo.setLevelOwn("一,二");
        singleDictionaryVo.setLevel("三,四");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,2", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("3,4", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("三,四", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert22() {
        MultipleDiffDelimiterDictionaryVo singleDictionaryVo = new MultipleDiffDelimiterDictionaryVo();
        singleDictionaryVo.setLevelOwn("1,2");
        singleDictionaryVo.setLevel("3,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("一，二", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("三，四", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("3,4", singleDictionaryVo.getLevel());
    }

    @Test
    void testConvert23() {
        SingleDictionaryVo singleDictionaryVo = new SingleDictionaryVo();
        singleDictionaryVo.setLevelOwn(Level.one.getValue());
        singleDictionaryVo.setLevel(Level.two.getValue());
        SingleDictionaryVo singleDictionaryVo1 = new SingleDictionaryVo();
        singleDictionaryVo1.setLevelOwn(Level.one.getValue());
        singleDictionaryVo1.setLevel(Level.two.getValue());
        ArrayList<SingleDictionaryVo> list = new ArrayList<>();
        list.add(singleDictionaryVo1);
        list.add(singleDictionaryVo);
        dictionaryConvertManagerUnderTest.convertList(list);
        Assertions.assertEquals("一", singleDictionaryVo.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo.getLevel());
        Assertions.assertEquals("一", singleDictionaryVo1.getLevelOwn());
        Assertions.assertEquals("二", singleDictionaryVo1.getLevelName());
        Assertions.assertEquals("2", singleDictionaryVo1.getLevel());
    }

    @Test
    void testConvert24() {
        MultipleDictionaryItemVo singleDictionaryVo = new MultipleDictionaryItemVo();

        singleDictionaryVo.setLevel("1,3,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,3,4",singleDictionaryVo.getLevel());
        Assertions.assertEquals(3,singleDictionaryVo.getLevelName().size());
        Assertions.assertEquals("1",singleDictionaryVo.getLevelName().get(0).getValue());
        Assertions.assertEquals("一",singleDictionaryVo.getLevelName().get(0).getName());
        Assertions.assertEquals("3",singleDictionaryVo.getLevelName().get(1).getValue());
        Assertions.assertEquals("三",singleDictionaryVo.getLevelName().get(1).getName());
        Assertions.assertEquals("4",singleDictionaryVo.getLevelName().get(2).getValue());
        Assertions.assertEquals("四",singleDictionaryVo.getLevelName().get(2).getName());
    }
    @Test
    void testConvert25() {
        MultipleDictionaryItemVo singleDictionaryVo = new MultipleDictionaryItemVo();

        singleDictionaryVo.setLevel("1,3,4,9");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,3,4,9",singleDictionaryVo.getLevel());
        Assertions.assertEquals(4,singleDictionaryVo.getLevelName().size());
        Assertions.assertEquals("1",singleDictionaryVo.getLevelName().get(0).getValue());
        Assertions.assertEquals("一",singleDictionaryVo.getLevelName().get(0).getName());
        Assertions.assertEquals("3",singleDictionaryVo.getLevelName().get(1).getValue());
        Assertions.assertEquals("三",singleDictionaryVo.getLevelName().get(1).getName());
        Assertions.assertEquals("4",singleDictionaryVo.getLevelName().get(2).getValue());
        Assertions.assertEquals("四",singleDictionaryVo.getLevelName().get(2).getName());
        Assertions.assertEquals("9",singleDictionaryVo.getLevelName().get(3).getValue());
        Assertions.assertEquals(null,singleDictionaryVo.getLevelName().get(3).getName());
    }

    @Test
    void testConvert26() {
        MultipleDictionaryItemErrorTypeVo singleDictionaryVo = new MultipleDictionaryItemErrorTypeVo();

        singleDictionaryVo.setLevel("1,3,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,3,4",singleDictionaryVo.getLevel());
        Assertions.assertEquals(3,singleDictionaryVo.getLevelName().size());
//        Assertions.assertEquals("1",singleDictionaryVo.getLevelName().get(0).getValue());
        Assertions.assertThrows(ClassCastException.class,()->singleDictionaryVo.getLevelName().get(0));

        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,3,4",singleDictionaryVo.getLevel());
        Assertions.assertEquals(3,singleDictionaryVo.getLevelName().size());
//        Assertions.assertEquals("1",singleDictionaryVo.getLevelName().get(0).getValue());
        Assertions.assertThrows(ClassCastException.class,()->singleDictionaryVo.getLevelName().get(0));
    }
    @Test
    void testConvert27() {
        MultipleDictionaryItemNotCollVo singleDictionaryVo = new MultipleDictionaryItemNotCollVo();

        singleDictionaryVo.setLevel("1,3,4");
        dictionaryConvertManagerUnderTest.convert(singleDictionaryVo);
        Assertions.assertEquals("1,3,4",singleDictionaryVo.getLevel());
        Assertions.assertEquals("1",singleDictionaryVo.getLevelName().getValue());
        Assertions.assertEquals("一",singleDictionaryVo.getLevelName().getName());
    }
    @Test
    void testDeept() {
        DeepDictionaryVo deepDictionaryVo = new DeepDictionaryVo("1", new DeepDictionaryVo("2", new DeepDictionaryVo("3", new DeepDictionaryVo("4"))));
        deepDictionaryVo.getDictionary1().getDictionary1().getDictionary1().setDictionary1(deepDictionaryVo);
        dictionaryConvertManagerUnderTest.convert(deepDictionaryVo);
        Assertions.assertEquals("一", deepDictionaryVo.getLevel());
        Assertions.assertEquals("二", deepDictionaryVo.getDictionary1().getLevel());
        Assertions.assertEquals("三", deepDictionaryVo.getDictionary1().getDictionary1().getLevel());
        Assertions.assertEquals("四", deepDictionaryVo.getDictionary1().getDictionary1().getDictionary1().getLevel());
    }

    @Test
    void testDeept1() {
        DeepDictionaryVo deepDictionaryVo = new DeepDictionaryVo("1", new DeepDictionaryVo("2", new DeepDictionaryVo("3", new DeepDictionaryVo("4"))));
        dictionaryConvertManagerUnderTest.convert(deepDictionaryVo);
        Assertions.assertEquals("一", deepDictionaryVo.getLevel());
        Assertions.assertEquals("二", deepDictionaryVo.getDictionary1().getLevel());
        Assertions.assertEquals("三", deepDictionaryVo.getDictionary1().getDictionary1().getLevel());
        Assertions.assertEquals("四", deepDictionaryVo.getDictionary1().getDictionary1().getDictionary1().getLevel());
    }
    @Test
    void testSuper(){
        ChildDictionaryVo childDictionaryVo = new ChildDictionaryVo();
        childDictionaryVo.setChildLevelOwn("1");
        dictionaryConvertManagerUnderTest.convert(childDictionaryVo);
        Assertions.assertEquals("一",childDictionaryVo.getChildLevelOwn());
    }

    @Test
    void testSuper1(){
        ChildDictionaryVo childDictionaryVo = new ChildDictionaryVo();
        childDictionaryVo.setLevelOwn("1");
        dictionaryConvertManagerUnderTest.convert(childDictionaryVo);
        Assertions.assertEquals("一",childDictionaryVo.getLevelOwn());
    }
    @Test
    void testSuper2(){
        ChildDictionaryVo childDictionaryVo = new ChildDictionaryVo();
        childDictionaryVo.setChildLevel("1");
        dictionaryConvertManagerUnderTest.convert(childDictionaryVo);
        Assertions.assertEquals("一",childDictionaryVo.getLevelName());
    }
    @Test
    void testSuper3(){
        ChildDictionaryVo childDictionaryVo = new ChildDictionaryVo();
        childDictionaryVo.setLevel("1");
        dictionaryConvertManagerUnderTest.convert(childDictionaryVo);
        Assertions.assertEquals("一",childDictionaryVo.getLevelName());
    }
    @Test
    void testSuper4(){
        ChildExtDictionaryVo childDictionaryVo = new ChildExtDictionaryVo();
        childDictionaryVo.setLevel("1");
        dictionaryConvertManagerUnderTest.convert(childDictionaryVo);
        Assertions.assertEquals("一",childDictionaryVo.getLevelName());
        Assertions.assertEquals("一",childDictionaryVo.getLevelName1());
    }
    @Test
    void testDictionaryItemField(){
        DictionaryItemFieldDto dto = new DictionaryItemFieldDto();
        dto.setLevel("1");
        dto.setLevelDictOwn("1");
        dictionaryConvertManagerUnderTest.convert(dto);
        Assertions.assertEquals("1",dto.getLevel());
        Assertions.assertEquals("1",dto.getLevelDict().getValue());
        Assertions.assertEquals("一",dto.getLevelDict().getName());
        Assertions.assertEquals("1",dto.getLevelDictOwn().getValue());
        Assertions.assertEquals("一",dto.getLevelDictOwn().getName());

    }
}
