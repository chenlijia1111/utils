package com.github.chenlijia1111.utils.common.enums;

/**
 * 响应状态枚举
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/21 0021 上午 11:34
 **/
public enum ResponseStatusEnum {

    SUCCESS(200,"请求成功"),
    FAIL(500,"请求失败"),
    EXISTS(501,"数据已存在"),
    NOT_EXISTS(502,"数据不存在"),
    UN_AUTHORIZED(401,"未授权"),
    UN_LOGINED(402,"未登陆");

    private int code;

    private String name;

    ResponseStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
