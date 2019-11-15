package com.github.chenlijia1111.util.core.reflect;

import com.github.chenlijia1111.utils.core.reflect.ClassUtil;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.database.pojo.dictionary.DictionaryFieldPojo;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/21 0021 下午 8:34
 **/
public class ClassScanTest {

    //测试类扫描
    @Test
    public void test1(){
        List<Class> classes = ClassUtil.doScan("com.alipay.api.msg", null, null, null);
        System.out.println(classes);
        System.out.println(classes.size());
    }

    @Test
    public void test2(){
        Class<ClassUtil> classUtilClass = ClassUtil.class;
        URL resource = classUtilClass.getClassLoader().getResource("com/github/chenlijia1111");
        System.out.println(resource);
    }

    @Test
    public void test3(){
        Class<DictionaryFieldPojo> dictionaryFieldPojoClass = DictionaryFieldPojo.class;
        System.out.println(dictionaryFieldPojoClass.getName());
        List<Field> allFields = PropertyUtil.getAllFields(dictionaryFieldPojoClass);
        for (Field field : allFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            System.out.println(fieldName);
            System.out.println(fieldType);
            System.out.println(fieldType == byte[].class);
        }
    }
}
