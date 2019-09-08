package com.github.chenlijia1111.utils.core.enums;

/**
 * 编码类型
 *
 * @author 陈礼佳
 * @since 2019/9/8 11:22
 */
public enum CharSetType {

    UTF8("UTF-8"), GBK("GBK");


    private String type;

    CharSetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
