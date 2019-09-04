package com.github.chenlijia1111.utils.office.excel;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.function.Function;

/**
 * 导出数据 对象
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/4 0004 上午 10:57
 **/
public class ExcelExportCellDate {

    /**
     * 属性名称
     *
     * @since 上午 10:58 2019/9/4 0004
     **/
    private String fieldName;

    /**
     * 属性值
     *
     * @since 上午 11:00 2019/9/4 0004
     **/
    private Object fieldValue;

    /**
     * 表头名称
     *
     * @since 上午 10:58 2019/9/4 0004
     **/
    private String headName;

    /**
     * 排序值
     *
     * @since 上午 10:59 2019/9/4 0004
     **/
    private int sort;

    /**
     * 数据转换器
     *
     * @since 上午 10:59 2019/9/4 0004
     **/
    private Function function;


    /**
     * 样式
     *
     * @since 上午 10:59 2019/9/4 0004
     **/
    private CellStyle cellStyle;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public void setFunction(Function function) {
        this.function = function;

        //如果转换器不为空 进行数据转换
        if (null != function && null != fieldValue) {
            this.fieldValue = function.apply(this.fieldValue);
        }
    }
}
