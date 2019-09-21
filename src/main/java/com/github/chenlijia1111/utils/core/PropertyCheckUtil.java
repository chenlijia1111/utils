package com.github.chenlijia1111.utils.core;


import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import com.github.chenlijia1111.utils.list.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 属性工具类
 * 用于递归获取 类的所有属性以及属性值
 * 校验属性
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/4/8 0008 上午 10:23
 **/
public class PropertyCheckUtil {


    /**
     * 获取父类属性在内的所有属性
     *
     * @param object 1
     * @author chenlijia
     * @since 下午 6:47 2019/5/21 0021
     **/
    public static Field[] getAllFields(Object object) {
        Class<?> aClass = object.getClass();

        return getAllFields(aClass);
    }

    /**
     * 获取父类属性在内的所有属性
     *
     * @param aClass 1
     * @author chenlijia
     * @since 下午 6:47 2019/5/21 0021
     **/
    public static Field[] getAllFields(Class aClass) {

        ArrayList<Field> fields = new ArrayList<>();
        while (aClass != null) {
            fields.addAll(Lists.asList(aClass.getDeclaredFields()));
            aClass = aClass.getSuperclass();
        }

        Field[] fields1 = new Field[fields.size()];
        fields.toArray(fields1);
        return fields1;
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
    public static boolean checkHasPropertity(Object object, Class objectClass, String propertyName) {
        if (Objects.nonNull(object) && Objects.nonNull(objectClass) && StringUtils.isNotEmpty(propertyName)) {
            try {
                Field declaredField = objectClass.getDeclaredField(propertyName);
                if (Objects.nonNull(declaredField)) {
                    return true;
                }
            } catch (NoSuchFieldException e) {
                //当前类没有这个属性,寻找父类
                objectClass = objectClass.getSuperclass();
                return checkHasPropertity(object, objectClass, propertyName);
            }
        }
        return false;
    }


    /**
     * 检测参数是否为空，为空则实例化对象
     * 防止参数为空
     *
     * @param t 1
     * @author chenlijia
     * @description TODO
     * @since 上午 11:44 2019/7/13 0013
     **/
    public static <T> T transferObjectNotNull(T t) {
        if (Objects.isNull(t)) {
            Class<?> aClass = t.getClass();
            try {
                t = (T) aClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 校验参数
     *
     * @param object          要校验的对象
     * @param checkFieldList  要校验的属性
     * @param ignoreFieldList 忽略的属性
     * @return com.github.chenlijia1111.utils.common.CheckResult
     * @since 下午 1:38 2019/9/9 0009
     **/
    public static Result checkProperty(Object object, List<String> checkFieldList, List<String> ignoreFieldList) {

        AssertUtil.isTrue(Objects.nonNull(object), "数据为空");

        //初始化，防止空指针
        checkFieldList = null == checkFieldList ? new ArrayList<>() : checkFieldList;
        ignoreFieldList = null == ignoreFieldList ? new ArrayList<>() : ignoreFieldList;

        //判断object 有没有PropertyCheck 的注解
        Class<?> aClass = object.getClass();
        PropertyCheck propertyCheck = aClass.getAnnotation(PropertyCheck.class);
        if (null != propertyCheck) {
            //需要检验的注解，如果属性带了这些注解的话，就需要参与检测
            Class[] annotationClassArray = propertyCheck.annotationClass();

            Field[] allFields = getAllFields(aClass);
            if (null != allFields && allFields.length > 0) {
                //开始判断
                for (Field field : allFields) {
                    field.setAccessible(true);
                    //属性名称
                    String fieldName = field.getName();

                    if (Lists.isNotEmpty(checkFieldList) && !checkFieldList.contains(fieldName)) {
                        continue;
                    }

                    if (Lists.isNotEmpty(ignoreFieldList) && ignoreFieldList.contains(fieldName)) {
                        continue;
                    }

                    if (Lists.isNotEmpty(checkFieldList) && checkFieldList.contains(fieldName) &&
                            !(Lists.isNotEmpty(ignoreFieldList) && ignoreFieldList.contains(fieldName))) {
                        //直接指明了要校验这个属性 且不存在要忽略这个属性
                        //直接开始校验
                        //判断属性类型
                        Class<?> fieldClass = field.getType();
                        //获取属性类型
                        try {
                            if (fieldClass == String.class) {
                                String str = (String) getFieldValue(object, aClass, fieldName);
                                if (StringUtils.isEmpty(str))
                                    return Result.failure(fieldName + "不能为空");
                            } else if (List.class.isAssignableFrom(fieldClass)) {
                                //说明是list集合
                                List list = (List) getFieldValue(object, aClass, fieldName);
                                if (null == list || list.size() == 0)
                                    return Result.failure(fieldName + "不能为空");
                            } else {
                                Object o = getFieldValue(object, aClass, fieldName);
                                if (null == o)
                                    return Result.failure(fieldName + "不能为空");
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (Class annotationClass : annotationClassArray) {
                            Annotation annotation = field.getAnnotation(annotationClass);
                            if (null != annotation) {
                                //开始进行判断
                                //判断属性类型
                                Class<?> fieldClass = field.getType();
                                //获取属性类型
                                try {
                                    if (fieldClass == String.class) {
                                        String str = (String) getFieldValue(object, aClass, fieldName);
                                        if (StringUtils.isEmpty(str))
                                            return Result.failure(fieldName + "不能为空");
                                    } else if (List.class.isAssignableFrom(fieldClass)) {
                                        //说明是list集合
                                        List list = (List) getFieldValue(object, aClass, fieldName);
                                        if (null == list || list.size() == 0)
                                            return Result.failure(fieldName + "不能为空");
                                    } else {
                                        Object o = getFieldValue(object, aClass, fieldName);
                                        if (null == o)
                                            return Result.failure(fieldName + "不能为空");
                                    }
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                }


                                break;
                            }
                        }
                    }


                }
            }
        }

        return Result.success("检测通过");
    }
}
