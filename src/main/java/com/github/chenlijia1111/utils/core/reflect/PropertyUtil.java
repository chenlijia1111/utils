package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    /**
     * 获取父类属性在内的所有属性
     *
     * @param object 1
     * @author chenlijia
     * @since 下午 6:47 2019/5/21 0021
     **/
    public static List<Field> getAllFields(Object object) {
        Class<?> aClass = object.getClass();

        return getAllFields(aClass);
    }


    /**
     * 获取属性值,递归获取父类的属性
     *
     * @param object 1
     * @author chenlijia
     * @since 下午 6:47 2019/5/21 0021
     **/
    public static Object getFieldValue(Object object, Class objectClass, String propertyName) throws NoSuchFieldException {

        if (Objects.nonNull(object) && Objects.nonNull(objectClass) && StringUtils.isNotEmpty(propertyName)) {
            try {
                Field declaredField = objectClass.getDeclaredField(propertyName);
                if (Objects.nonNull(declaredField)) {
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(object);
                    return o;
                }
            } catch (NoSuchFieldException e) {
                //当前类没有这个属性,寻找父类
                objectClass = objectClass.getSuperclass();
                return getFieldValue(object, objectClass, propertyName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchFieldException("找不到这个属性");
    }


    /**
     * 递归判断是否有这个属性
     *
     * @param object       1
     * @param objectClass  2
     * @param propertyName 3
     * @author chenlijia
     * @description TODO
     * @since 上午 10:47 2019/6/29 0029
     **/
    public static boolean hasProperty(Object object, Class objectClass, String propertyName) {
        if (Objects.nonNull(object) && Objects.nonNull(objectClass) && StringUtils.isNotEmpty(propertyName)) {
            try {
                Field declaredField = objectClass.getDeclaredField(propertyName);
                if (Objects.nonNull(declaredField)) {
                    return true;
                }
            } catch (NoSuchFieldException e) {
                //当前类没有这个属性,寻找父类
                objectClass = objectClass.getSuperclass();
                return hasProperty(object, objectClass, propertyName);
            }
        }
        return false;
    }


    /**
     * 反射 调取属性的 set 方法
     *
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @param object        对象
     * @return void
     * @since 下午 1:17 2019/9/25 0025
     **/
    public static void propertyWithSet(String propertyName, Object propertyValue, Object object) {
        Class<?> aClass = object.getClass();
        try {
            Method setMethod = MethodUtil.fieldWriteMethod(propertyName, aClass);
            if (Objects.nonNull(setMethod)) {
                setMethod.invoke(object, propertyValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


}
