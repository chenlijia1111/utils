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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyCheck {

    /**
     * 需要关注的注解
     * 凡是有这些注解的属性都需要进行验证
     *
     * @return java.lang.Class
     * @since 下午 1:08 2019/9/9 0009
     **/
    Class[] annotationClass();

}
