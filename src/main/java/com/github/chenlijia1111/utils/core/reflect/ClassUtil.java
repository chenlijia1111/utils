package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.common.AssertUtil;

/**
 * 类反射工具
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/24 0024 下午 4:58
 **/
public class ClassUtil {


    /**
     * 获取对象的类的完整路径加名字
     * 如 com.github.chenlijia1111.utils.core.reflect.ClassUtil
     *
     * @param obj 1
     * @return java.lang.String
     * @since 下午 5:00 2019/9/24 0024
     **/
    public static String classPathName(Object obj) {
        AssertUtil.isTrue(null != obj, "对象为空");
        return obj.getClass().getName();
    }

    /**
     * 获取对象的类的名称
     * 如 ClassUtil
     *
     * @param obj 1
     * @return java.lang.String
     * @since 下午 5:00 2019/9/24 0024
     **/
    public static String className(Object obj) {
        AssertUtil.isTrue(null != obj, "对象为空");
        return obj.getClass().getSimpleName();
    }

}
