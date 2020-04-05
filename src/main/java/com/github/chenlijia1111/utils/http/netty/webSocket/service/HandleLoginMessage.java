package com.github.chenlijia1111.utils.http.netty.webSocket.service;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.netty.webSocket.enums.MessageTypeEnum;
import com.github.chenlijia1111.utils.http.netty.webSocket.pojo.LoginMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Objects;

/**
 * 处理登录消息
 *
 * @author 陈礼佳
 * @since 2020/4/5 8:40
 */
public class HandleLoginMessage implements IHandleMessage {

    /**
     * 处理登录消息
     *
     * @param webSocketFrame
     */
    @Override
    public void handleMessage(Channel channel, TextWebSocketFrame webSocketFrame) {
        if (null != channel && null != webSocketFrame) {
            String text = webSocketFrame.text();
            if (StringUtils.isNotEmpty(text) &&
                    Objects.equals(MessageTypeEnum.LOGIN.getType(),
                            JSONUtil.strToJsonNode(text).get("type").asText())) {
                //是登录操作，开始处理
                LoginMessage loginMessage = JSONUtil.strToObj(text, LoginMessage.class);
                MessageDealService.getInstance().addChannel(loginMessage.getUserId(), channel);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.objToStr(Result.success("登陆成功"))));
            }
        }
    }
}
