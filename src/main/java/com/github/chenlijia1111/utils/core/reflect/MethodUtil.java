package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.list.Lists;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射 方法工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/24 0024 下午 4:58
 **/
public class MethodUtil {


    /**
     * 获取对象的所有方法
     * 循环查找父类的所有方法
     *
     * @param cls 1
     * @return java.util.List<java.lang.reflect.Method>
     * @since 下午 5:10 2019/9/24 0024
     **/
    public static List<Method> getMethods(Class cls) {

        AssertUtil.isTrue(null != cls, "对象为空");

        //method 集合
        List<Method> methodList = Lists.newArrayList();
        //获取所有的方法
        methodList.addAll(Lists.asList(cls.getDeclaredMethods()));

        //判断有没有父类,循环获取父类的方法
        Class superclass = cls.getSuperclass();
        while (null != superclass) {
            methodList.addAll(Lists.asList(superclass.getDeclaredMethods()));
            superclass = superclass.getSuperclass();
        }
        return methodList;
    }


    /**
     * 获取字段的 get 方法
     *
     * @param fieldName 1
     * @param cls       2
     * @return java.lang.reflect.Method
     * @since 上午 10:18 2019/9/29 0029
     **/
    public static Method fieldReadMethod(String fieldName, Class cls) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, cls);
            Method readMethod = propertyDescriptor.getReadMethod();
            return readMethod;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取字段的 set 方法
     *
     * @param fieldName 1
     * @param cls       2
     * @return java.lang.reflect.Method
     * @since 上午 10:18 2019/9/29 0029
     **/
    public static Method fieldWriteMethod(String fieldName, Class cls) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, cls);
            Method readMethod = propertyDescriptor.getWriteMethod();
            return readMethod;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
