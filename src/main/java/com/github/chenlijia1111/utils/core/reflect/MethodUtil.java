package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

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
     * <p>
     * boolean 的 get 方法比较特殊 is 开头的
     *
     * @param fieldName 1
     * @param cls       2
     * @return java.lang.reflect.Method
     * @since 上午 10:18 2019/9/29 0029
     **/
    public static Method fieldReadMethod(String fieldName, Class cls) {
        if (StringUtils.isNotEmpty(fieldName) && Objects.nonNull(cls)) {
            //get 方法
            try {
                Field declaredField = cls.getDeclaredField(fieldName);
                Class<?> fieldType = declaredField.getType();
                StringBuilder sb = new StringBuilder();
                if (Objects.equals(boolean.class, fieldType)) {
                    sb.append("is");
                } else {
                    sb.append("get");
                }
                sb.append(fieldName.substring(0, 1).toUpperCase());
                sb.append(fieldName.substring(1));
                //方法名称
                String methodName = sb.toString();
                Method method = cls.getMethod(methodName);
                return method;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取字段的 set 方法
     * <p>
     *
     * @param fieldName 1
     * @param cls       2
     * @return java.lang.reflect.Method
     * @since 上午 10:18 2019/9/29 0029
     **/
    public static Method fieldWriteMethod(String fieldName, Class cls) {
        try {
            Field declaredField = cls.getDeclaredField(fieldName);
            Class<?> fieldType = declaredField.getType();
            StringBuilder sb = new StringBuilder();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            //方法名称
            String methodName = sb.toString();
            Method method = cls.getMethod(methodName, fieldType);
            return method;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
