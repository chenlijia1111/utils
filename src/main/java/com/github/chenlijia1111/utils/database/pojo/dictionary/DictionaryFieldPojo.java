package com.github.chenlijia1111.utils.database.pojo.dictionary;

import com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField;

/**
 * 数据字段字段对象
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 1:23
 **/
public class DictionaryFieldPojo {
    /**
     * 表名
     *
     * @since 下午 1:30 2019/11/14 0014
     **/
    private String tableName;

    /**
     * 表备注
     *
     * @since 下午 1:30 2019/11/14 0014
     **/
    private String tableComment;


    /**
     * 字段名
     *
     * @since 下午 1:30 2019/11/14 0014
     **/
    @ExcelExportField(titleHeadName = "字段名", sort = 0, cellWidth = 400000)
    private String fieldName;

    /**
     * 字段类型
     *
     * @since 下午 1:30 2019/11/14 0014
     **/
    @ExcelExportField(titleHeadName = "字段类型", sort = 1, cellWidth = 400000)
    private String fieldType;

    /**
     * 字段备注
     *
     * @since 下午 1:30 2019/11/14 0014
     **/
    @ExcelExportField(titleHeadName = "字段备注", sort = 2, cellWidth = 400000)
    private String fieldComment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }


    @Override
    public String toString() {
        return "DictionaryFieldPojo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldComment='" + fieldComment + '\'' +
                '}';
    }
}
