package com.github.chenlijia1111.utils.office.excel.annos;

import com.github.chenlijia1111.utils.common.constant.BooleanConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

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

    /**
     * 默认不做转换
     *
     * @return
     */
    Class<? extends Function> convert() default NoConvert.class;

    /**
     * 是否是图片
     * 如果为是的话
     * 会进行判断，如果当前属性为 String 且 前缀以 http:// 或者 https:// 开头，就会进行请求网络图片进行放入
     * 否则的话，就会当作是本地的文件路径，以本地文件的形式进行读取并放入
     * @return
     */
    int imageStatus() default BooleanConstant.NO_INTEGER;


    /**
     * 默认转换器，默认不做转换
     */
    class NoConvert implements Function<Object, Object> {

        @Override
        public Object apply(Object o) {
            return o;
        }
    }


}
