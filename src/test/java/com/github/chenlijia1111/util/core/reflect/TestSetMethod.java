package com.github.chenlijia1111.util.core.reflect;

import com.github.chenlijia1111.utils.core.reflect.MethodUtil;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * 测试获取类的属性的set方法
 *
 * @author Chen LiJia
 * @since 2020/7/31
 */
public class TestSetMethod {

    @Test
    public void test1() {
        Class<Admin> adminClass = Admin.class;
        Method name = MethodUtil.fieldWriteMethod("name", adminClass);
        System.out.println(name);

        Method status = MethodUtil.fieldWriteMethod("statue", adminClass);
        System.out.println(status);

        Method status1 = MethodUtil.fieldWriteMethod("status1", adminClass);
        System.out.println(status1);

    }

    @Test
    public void test2() {
        Class<Admin> adminClass = Admin.class;
        Method name = MethodUtil.fieldReadMethod("name", adminClass);
        System.out.println(name);

        Method status = MethodUtil.fieldReadMethod("statue", adminClass);
        System.out.println(status);

        Method status1 = MethodUtil.fieldReadMethod("status1", adminClass);
        System.out.println(status1);

    }


}
