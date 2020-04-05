package com.github.chenlijia1111.utils.http.netty.webSocket.enums;

/**
 * 消息类型
 *
 * @author 陈礼佳
 * @since 2020/4/4 16:50
 */
public enum MessageTypeEnum {

    LOGIN("login"),
    TEXT("text"),
    AUDIO("audio"),
    VIDEO("video"),
    ;

    private String type;

    MessageTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
