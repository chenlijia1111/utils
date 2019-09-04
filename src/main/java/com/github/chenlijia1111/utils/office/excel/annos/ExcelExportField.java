package com.github.chenlijia1111.utils.office.excel.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 导出字段注解
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 8:42
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExportField {

    /**
     * 表格表头名称
     *
     * @return java.lang.String
     * @since 下午 8:44 2019/9/3 0003
     **/
    String titleHeadName() default "";


    /**
     * 表格宽度
     *
     * @return int
     * @since 下午 8:45 2019/9/3 0003
     **/
    int cellWidth() default 25;

    /**
     * 排序值
     *
     * @return int
     * @since 上午 10:55 2019/9/4 0004
     **/
    int sort();


}
