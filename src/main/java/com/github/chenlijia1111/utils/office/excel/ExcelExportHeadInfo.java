package com.github.chenlijia1111.utils.office.excel;

/**
 * 导出excel 表头数据
 * 用于进行表头排序
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/4 0004 下午 1:01
 **/
public class ExcelExportHeadInfo {

    /**
     * 属性名称
     *
     * @since 上午 10:58 2019/9/4 0004
     **/
    private String fieldName;


    /**
     * 表头名称
     *
     * @since 上午 10:58 2019/9/4 0004
     **/
    private String headName;

    /**
     * 表头宽度
     *
     * @since 下午 1:31 2019/9/4 0004
     **/
    private int cellWidth;


    /**
     * 排序值
     *
     * @since 上午 10:59 2019/9/4 0004
     **/
    private int sort;

    public ExcelExportHeadInfo(String fieldName, String headName, int sort, int cellWidth) {
        this.fieldName = fieldName;
        this.headName = headName;
        this.sort = sort;
        this.cellWidth = cellWidth;
    }

    public ExcelExportHeadInfo() {
    }

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

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }
}
