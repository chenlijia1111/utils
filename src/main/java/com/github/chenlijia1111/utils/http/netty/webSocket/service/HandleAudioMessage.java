package com.github.chenlijia1111.utils.http.netty.webSocket.service;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.netty.webSocket.enums.MessageTypeEnum;
import com.github.chenlijia1111.utils.http.netty.webSocket.pojo.AudioMessage;
import com.github.chenlijia1111.utils.http.netty.webSocket.pojo.TextMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;

import java.util.Objects;


/**
 * 处理音频消息
 *
 * @author 陈礼佳
 * @since 2020/4/5 8:40
 */
public class HandleAudioMessage implements IHandleMessage {

    private static final Logger log = new LogUtil(HandleAudioMessage.class);

    /**
     * 处理音频信息
     *
     * @param channel
     * @param webSocketFrame
     */
    @Override
    public void handleMessage(Channel channel, TextWebSocketFrame webSocketFrame) {
        if (null != channel && null != webSocketFrame) {
            String text = webSocketFrame.text();
            if (StringUtils.isNotEmpty(text) &&
                    Objects.equals(MessageTypeEnum.AUDIO.getType(),
                            JSONUtil.strToJsonNode(text).get("type").asText())) {
                //是音频操作，开始处理
                AudioMessage audioMessage = JSONUtil.strToObj(text, AudioMessage.class);
                if(StringUtils.isNotEmpty(audioMessage.getToUser())){
                    //这里只处理点对点的消息，如果要处理群组消息，需要自己实现，因为在内存里存太多数据不好。
                    //查找channel发送消息
                    ChannelId toUserChannelId = MessageDealService.USER_TO_CHANNEL_LONG_KEY.get(audioMessage.getToUser());
                    if (Objects.nonNull(toUserChannelId)) {
                        //发送了消息
                        //注意，这里不要直接把传过来的TextWebSocketFrame直接转发，要创建一个新的对象，不然会报错
                        //原因是 TextWebSocketFrame 内部其实是一个ByteBuf
                        //在netty4中，对象的生命周期由引用计数器控制，ByteBuf就是如此，每个对象的初始化引用计数加1，
                        //调用一次release方法，引用计数器会减1，当尝试访问计数器为0时，对象时，会抛出IllegalReferenceCountException
                        MessageDealService.CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(text), channel1 -> Objects.equals(channel1.id(), toUserChannelId));
                    }
                }
            }
        }
    }
}
