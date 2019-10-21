package com.github.chenlijia1111.util.core.reflect;

import com.github.chenlijia1111.utils.core.reflect.ClassUtil;
import org.junit.Test;

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
}
