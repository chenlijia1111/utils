package com.github.chenlijia1111.utils.core;


import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import com.github.chenlijia1111.utils.core.enums.PropertyCheckType;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.list.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 属性工具类
 * 用于递归获取 类的所有属性以及属性值
 * 校验属性
 * {@link PropertyCheck}
 * 只校验有这个注解的属性
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/4/8 0008 上午 10:23
 **/
public class PropertyCheckUtil {


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
        return transferObjectNotNull(t, true);
    }

    /**
     * 检测参数是否为空，为空则实例化对象
     * 防止参数为空
     *
     * @param t                 1
     * @param spaceStringToNull 空字符转null
     * @author chenlijia
     * @description TODO
     * @since 上午 11:44 2019/7/13 0013
     **/
    public static <T> T transferObjectNotNull(T t, boolean spaceStringToNull) {
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

        if (spaceStringToNull) {
            List<Field> allFields = PropertyUtil.getAllFields(t);
            if (Lists.isNotEmpty(allFields)) {
                for (Field allField : allFields) {
                    allField.setAccessible(true);
                    Class<?> type = allField.getType();
                    if (String.class == type) {
                        try {
                            Object fieldValue = PropertyUtil.getFieldValue(t, t.getClass(), allField.getName());
                            if (Objects.nonNull(fieldValue) && fieldValue.equals("")) {
                                allField.set(t, null);
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return t;
    }

    /**
     * 校验所有带了注解的属性
     *
     * @param object 1
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 下午 1:07 2019/9/25 0025
     **/
    public static Result checkProperty(Object object) {
        return checkProperty(object, null, null);
    }

    /**
     * 指定要校验的属性
     *
     * @param object         1
     * @param checkFieldList 2
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 下午 1:07 2019/9/25 0025
     **/
    public static Result checkProperty(Object object, List<String> checkFieldList) {
        return checkProperty(object, checkFieldList, null);
    }

    /**
     * 指定要忽略校验的属性
     *
     * @param object          1
     * @param ignoreFieldList 2
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 下午 1:07 2019/9/25 0025
     **/
    public static Result checkPropertyWithIgnore(Object object, List<String> ignoreFieldList) {
        return checkProperty(object, null, ignoreFieldList);
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
    private static Result checkProperty(Object object, List<String> checkFieldList, List<String> ignoreFieldList) {

        AssertUtil.isTrue(Objects.nonNull(object), "数据为空");

        //初始化，防止空指针
        checkFieldList = null == checkFieldList ? new ArrayList<>() : checkFieldList;
        ignoreFieldList = null == ignoreFieldList ? new ArrayList<>() : ignoreFieldList;

        //判断object 有没有PropertyCheck 的注解
        Class<?> aClass = object.getClass();

        //获取所有属性
        List<Field> allFields = PropertyUtil.getAllFields(aClass);
        if (Lists.isNotEmpty(allFields)) {
            //开始判断
            for (Field field : allFields) {
                field.setAccessible(true);
                //属性名称
                String fieldName = field.getName();

                //只处理有注解的字段
                PropertyCheck propertyCheck = field.getAnnotation(PropertyCheck.class);
                if (null == propertyCheck) {
                    continue;
                }

                String name = propertyCheck.name();

                //指明了只处理某些字段
                if (Lists.isNotEmpty(checkFieldList) && !checkFieldList.contains(fieldName)) {
                    continue;
                }
                //指明了不处理某些字段
                if (Lists.isNotEmpty(ignoreFieldList) && ignoreFieldList.contains(fieldName)) {
                    continue;
                }

                //直接开始校验
                //判断属性类型
                Class<?> fieldClass = field.getType();
                //获取属性类型
                try {

                    Object fieldValue = PropertyUtil.getFieldValue(object, aClass, fieldName);
                    //判断有没有申明校验类型
                    PropertyCheckType propertyCheckType = propertyCheck.checkType();
                    if (null != propertyCheck && !Objects.equals(propertyCheck, PropertyCheckType.NO_CHECK)) {
                        Predicate checkFunction = propertyCheckType.getCheckFunction();
                        boolean test = checkFunction.test(fieldValue);
                        if (!test) {
                            return Result.failure(name + "不合法");
                        }
                    }
                    //判断有没有申明校验方法
                    Class<? extends Predicate> predicateClass = propertyCheck.checkFunction();
                    if (Objects.nonNull(predicateClass)) {
                        Predicate predicate = predicateClass.newInstance();
                        boolean test = predicate.test(fieldValue);
                        if (!test) {
                            return Result.failure(name + "不合法");
                        }
                    }

                    if (fieldClass == String.class) {
                        String str = (String) PropertyUtil.getFieldValue(object, aClass, fieldName);
                        if (StringUtils.isEmpty(str)) {
                            return Result.failure(name + "不能为空");
                        }

                        //判断注解有没有声明正则
                        String s = propertyCheck.regMatcher();
                        if (StringUtils.isNotEmpty(s)) {
                            boolean matches = Pattern.matches(s, str);
                            if (!matches) {
                                return Result.failure(name + "不合法");
                            }
                        }

                    } else if (List.class.isAssignableFrom(fieldClass)) {
                        //说明是list集合
                        List list = (List) PropertyUtil.getFieldValue(object, aClass, fieldName);
                        if (null == list || list.size() == 0)
                            return Result.failure(name + "不能为空");
                    } else {
                        Object o = PropertyUtil.getFieldValue(object, aClass, fieldName);
                        if (null == o)
                            return Result.failure(name + "不能为空");
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success("检测通过");
    }
}
