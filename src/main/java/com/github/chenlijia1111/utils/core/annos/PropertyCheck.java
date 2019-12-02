package com.github.chenlijia1111.utils.core.annos;

import com.github.chenlijia1111.utils.core.enums.PropertyCheckType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

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


    /**
     * 校验类型,每一个校验类型都会有对应的校验方法
     *
     * @return com.github.chenlijia1111.utils.core.enums.PropertyCheckType
     * @since 下午 12:40 2019/9/27 0027
     **/
    PropertyCheckType checkType() default PropertyCheckType.NO_CHECK;


    /**
     * 校验方法 返回true 则校验通过
     * 以下列举了常用的参数校验方法
     *
     * @return java.lang.Class<? extends java.util.function.Predicate>
     * @see com.github.chenlijia1111.utils.core.commonCheckFunction.PositiveNumberCheck 正整数校验
     * @see com.github.chenlijia1111.utils.core.commonCheckFunction.PriceCheck 价格校验
     * @see com.github.chenlijia1111.utils.core.commonCheckFunction.RatioCheck 比率校验
     * @see com.github.chenlijia1111.utils.core.commonCheckFunction.StateCheck 状态校验
     * @since 下午 12:41 2019/9/27 0027
     **/
    Class<? extends Predicate> checkFunction() default DefaultPredicate.class;


    /**
     * 默认的校验方法
     * 直接返回true 不做校验
     *
     * @since 下午 1:13 2019/9/27 0027
     **/
    class DefaultPredicate implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return true;
        }
    }

}
