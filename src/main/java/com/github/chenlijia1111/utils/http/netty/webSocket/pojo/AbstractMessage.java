package com.github.chenlijia1111.utils.http.netty.webSocket.pojo;

import com.github.chenlijia1111.utils.common.constant.BooleanConstant;
import com.github.chenlijia1111.utils.core.IDUtil;

import java.util.Date;

/**
 * 消息父类
 * @author 陈礼佳
 * @since 2020/4/4 16:30
 */
public abstract class AbstractMessage {

    private static final IDUtil idUtil = new IDUtil(10,10);

    /**
     * 消息id
     */
    private String id = String.valueOf(idUtil.nextId());

    /**
     * 消息类型
     * text
     * audio
     * video
     */
    private String type;

    /**
     * 发送时间
     */
    private Date sendTime = new Date();

    /**
     * 发送状态 0否1是
     */
    private Integer sendStatus = BooleanConstant.NO_INTEGER;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
}
