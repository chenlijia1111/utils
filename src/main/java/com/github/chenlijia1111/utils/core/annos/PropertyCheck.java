package com.github.chenlijia1111.utils.core.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验字段 注解
 * 用于属性
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/5 0005 下午 1:34
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyCheck {

    /**
     * 字段名称
     *
     * @return java.lang.String
     * @since 下午 12:48 2019/9/25 0025
     **/
    String name();

    /**
     * 字段匹配正则 只有在字段数据类型是字符串的时候才进行验证
     *
     * @since 下午 12:48 2019/9/25 0025
     **/
    String regMatcher() default "";

}
