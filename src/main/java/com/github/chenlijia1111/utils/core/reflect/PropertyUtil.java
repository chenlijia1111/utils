package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.list.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射属性工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/24 0024 下午 4:59
 **/
public class PropertyUtil {

    /**
     * 获取父类属性在内的所有属性
     *
     * @param aClass 1
     * @author chenlijia
     * @since 下午 6:47 2019/5/21 0021
     **/
    public static List<Field> getAllFields(Class aClass) {

        List<Field> fieldList = new ArrayList<>();
        while (aClass != null) {
            fieldList.addAll(Lists.asList(aClass.getDeclaredFields()));
            aClass = aClass.getSuperclass();
        }
        return fieldList;
    }


    public static Object getFieldValue(){
        return null;
    }


}
