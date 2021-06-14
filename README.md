# 字典翻译
## 实现原理

原理上，采用反射的方式，获取被标记`@Dictionary`注解的字段的原始值，并将翻译后的值设置回去
所有获取值与设置值的操作都优先使用`setter` `getter`方法，找不到方法时，才会直接访问字段

## 功能&特性

- 支持指定原始值所在字段
- 支持指定翻译后的值保存的字段
- 支持枚举作为字段数据来源
- 支持外部字典数据来源
- 支持以分隔符分割的字典值翻译,可指定翻译后的分隔符
- 支持字典项名称翻译为字典项值

## 快速开始

### 导入依赖
```xml
<dependency>
  <groupId>io.github.snailrend.dictionary</groupId>
  <artifactId>dictionary</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 编写枚举类
```java
public enum Level implements DictionaryItem {
    one("1", "一"),
    two("2", "二"),
    three("3", "三"),
    four("4", "四"),
    five("5", "五");
    private String value;
    private String name;

    Level(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 编写需要翻译的类
```java

public class SingleDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
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
```

### 执行翻译
```java
class DictionaryConvertManagerTest {

    //如果是在 Spring 环境，可以将此对象注册为 Bean
    private DictionaryConvertManager dictionaryConvertManagerUnderTest = new DictionaryConvertManager();
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
}
```

## 扩展
### 外部字典数据
也可以在创建`DictionaryConvertManager`时注册外部的字典数据：
```java
//如果是在 Spring 环境，可以将此对象注册为 Bean
DictionaryConvertManager dictionaryConvertManager = new DictionaryConvertManager((dictionaryName, loaderParams) -> {
    return loadData(dictionaryName, loaderParams);
});
```
### 深层次翻译
当字段类型对应的类上有标记`@DictionaryConvertible`注解时，此字段的值若是非空，也会对值对象中的字段进行翻译
```java

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

```
### 字典项名称翻译为字典项值
```java
public class SingleNameToValueDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class,convertType = ConvertType.NAME_TO_VALUE)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName",convertType = ConvertType.NAME_TO_VALUE)
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

class DictionaryConvertManagerTest {

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
}
```
### 翻译多个字典值
```java
public class MultipleDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class, valueDelimiter = ",")
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName", valueDelimiter = ",")
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

class DictionaryConvertManagerTest {
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
}
```
### 翻译多个字典项名称为字典值
```java

public class MultipleNameToValueDictionaryVo {
    @Dictionary(dictionaryEnum = Level.class,nameDelimiter= ",",convertType = ConvertType.NAME_TO_VALUE)
    private String levelOwn;
    @Dictionary(dictionaryEnum = Level.class,nameDelimiter= ",",toField = "levelName",convertType = ConvertType.NAME_TO_VALUE)
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

class DictionaryConvertManagerTest {
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
}
```

### 将字典值翻译成字典项实例
```java

public class SingleDictionaryItemVo {
    @Dictionary(dictionaryEnum = Level.class)
    private DictionaryItem levelOwn;
    @Dictionary(dictionaryEnum = Level.class,toField = "levelName")
    private String level;
    private DictionaryItem levelName;

    public DictionaryItem getLevelOwn() {
        return levelOwn;
    }

    public void setLevelOwn(DictionaryItem levelOwn) {
        this.levelOwn = levelOwn;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DictionaryItem getLevelName() {
        return levelName;
    }

    public void setLevelName(DictionaryItem levelName) {
        this.levelName = levelName;
    }
}

class DictionaryConvertManagerTest {
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
}
```
