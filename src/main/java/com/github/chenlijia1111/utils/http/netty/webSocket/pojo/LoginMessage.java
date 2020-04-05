package com.github.chenlijia1111.utils.http.netty.webSocket.pojo;

import com.github.chenlijia1111.utils.http.netty.webSocket.enums.MessageTypeEnum;

/**
 * 登录消息
 * @author 陈礼佳
 * @since 2020/4/4 16:31
 */
public class LoginMessage extends AbstractMessage {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户密码
     */
    private String password;

    public LoginMessage() {
        this.setType(MessageTypeEnum.LOGIN.getType());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
