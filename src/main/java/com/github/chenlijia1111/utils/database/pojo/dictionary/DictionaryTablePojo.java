package com.github.chenlijia1111.utils.database.pojo.dictionary;

import java.util.List;

/**
 * 数据字典表对象
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 1:23
 **/
public class DictionaryTablePojo {

    /**
     * 表名
     *
     * @since 下午 1:26 2019/11/14 0014
     **/
    private String tableName;

    /**
     * 表注释
     *
     * @since 下午 1:26 2019/11/14 0014
     **/
    private String tableComment;

    /**
     * 字段列表
     *
     * @since 下午 1:26 2019/11/14 0014
     **/
    private List<DictionaryFieldPojo> fieldList;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment == null ? "" : tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<DictionaryFieldPojo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DictionaryFieldPojo> fieldList) {
        this.fieldList = fieldList;
    }

    @Override
    public String toString() {
        return "DictionaryTablePojo{" +
                "tableName='" + tableName + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }
}
