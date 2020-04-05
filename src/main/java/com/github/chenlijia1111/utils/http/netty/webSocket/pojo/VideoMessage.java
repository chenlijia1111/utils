package com.github.chenlijia1111.utils.http.netty.webSocket.pojo;

import com.github.chenlijia1111.utils.http.netty.webSocket.enums.MessageTypeEnum;

/**
 * @author 陈礼佳
 * @since 2020/4/4 16:31
 */
public class VideoMessage extends AbstractMessage {

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

    /**
     * 视频地址
     */
    private String videoUrl;

    public VideoMessage() {
        this.setType(MessageTypeEnum.VIDEO.getType());
    }

    
    public String getFromUser() {
        return fromUser;
    }

    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    
    public String getToUser() {
        return toUser;
    }

    
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    
    public String getToGroup() {
        return toGroup;
    }

    
    public void setToGroup(String toGroup) {
        this.toGroup = toGroup;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
