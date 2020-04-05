package com.github.chenlijia1111.utils.http.netty.webSocket.pojo;

import com.github.chenlijia1111.utils.http.netty.webSocket.enums.MessageTypeEnum;

/**
 * 文本消息
 * @author 陈礼佳
 * @since 2020/4/4 16:31
 */
public class TextMessage extends AbstractMessage {

    /**
     * 发送人的id
     */
    private String fromUser;

    /**
     * 接收人的Id
     */
    private String toUser;

    /**
     * 接收群组的id
     */
    private String toGroup;

    private String text;

    public TextMessage() {
        this.setType(MessageTypeEnum.TEXT.getType());
    }

    
    public String getFromUser() {
        return fromUser;
    }

    
    public TextMessage setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    
    public String getToUser() {
        return toUser;
    }

    
    public TextMessage setToUser(String toUser) {
        this.toUser = toUser;
        return this;
    }

    
    public String getToGroup() {
        return toGroup;
    }

    
    public TextMessage setToGroup(String toGroup) {
        this.toGroup = toGroup;
        return this;
    }

    public String getText() {
        return text;
    }

    public TextMessage setText(String text) {
        this.text = text;
        return this;
    }
}
