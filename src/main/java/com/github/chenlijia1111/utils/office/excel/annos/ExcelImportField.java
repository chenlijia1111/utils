package com.github.chenlijia1111.utils.office.excel.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel导入注解
 *
 * @since 下午 5:41 2019/10/11 0011
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImportField {

    /**
     * 字段所对应的的列下标
     *
     * @since 下午 5:44 2019/10/11 0011
     **/
    int cellIndex();

}
