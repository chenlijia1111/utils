package com.github.chenlijia1111.utils.http.netty.webSocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.netty.webSocket.pojo.TextMessage;
import com.github.chenlijia1111.utils.list.Sets;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;

/**
 * 消息处理业务
 *
 * @author 陈礼佳
 * @since 2020/4/4 17:05
 */
public class MessageDealService {

    /**
     * 存储所有channel 这是netty封装好的用于保存channel的集合
     */
    public static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 用户Id => channel id 映射
     */
    public static final Map<String, ChannelId> USER_TO_CHANNEL_LONG_KEY = new HashMap<>();

    /**
     * channel id => 用户Id 映射
     */
    public static final Map<ChannelId, String> CHANNEL_LONG_KEY_TO_USER = new HashMap<>();

    /**
     * 处理消息的实现类
     */
    private static final Set<IHandleMessage> HANDLE_MESSAGE_SET = new HashSet<>();

    static {
        //注入默认消息处理类
        HANDLE_MESSAGE_SET.add(new HandleLoginMessage());
        HANDLE_MESSAGE_SET.add(new HandleTextMessage());
        HANDLE_MESSAGE_SET.add(new HandleAudioMessage());
        HANDLE_MESSAGE_SET.add(new HandleVideoMessage());
    }


    private static volatile MessageDealService messageDealService;

    private MessageDealService() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static MessageDealService getInstance() {
        if (null == messageDealService) {
            synchronized (MessageDealService.class) {
                if (null == messageDealService) {
                    messageDealService = new MessageDealService();
                }
            }
        }
        return messageDealService;
    }

    /**
     * 注入消息处理类
     * @param handleMessages
     */
    public void addHandleMessage(IHandleMessage... handleMessages){
        if(null != handleMessages){
            HANDLE_MESSAGE_SET.addAll(Sets.asSets(handleMessages));
        }
    }


    /**
     * 添加连接
     * 登录
     *
     * @param userId
     * @param channel
     */
    public void addChannel(String userId, Channel channel) {
        if (StringUtils.isNotEmpty(userId) && Objects.nonNull(channel)) {
            USER_TO_CHANNEL_LONG_KEY.put(userId, channel.id());
            CHANNEL_LONG_KEY_TO_USER.put(channel.id(), userId);
            CHANNEL_GROUP.add(channel);
        }
    }

    /**
     * 移除连接
     *
     * @param channel
     */
    public void removeChannel(Channel channel) {
        if (Objects.nonNull(channel)) {
            CHANNEL_GROUP.remove(channel);
            String userId = CHANNEL_LONG_KEY_TO_USER.get(channel.id().asLongText());
            CHANNEL_LONG_KEY_TO_USER.remove(channel.id().asLongText());
            USER_TO_CHANNEL_LONG_KEY.remove(userId);
        }
    }


    /**
     * 将消息发送给指定的用户
     *
     * @param fromUserId
     * @param toUserId
     * @param text
     */
    public void sendTextToUser(String fromUserId, String toUserId, String text) {
        if (StringUtils.isNotEmpty(fromUserId, toUserId, text)) {
            //找到对应的chanel
            ChannelId toUserChannelId = USER_TO_CHANNEL_LONG_KEY.get(toUserId);
            if (Objects.nonNull(toUserChannelId)) {
                //构建发送对象
                TextMessage textMessage = new TextMessage().setFromUser(fromUserId).
                        setToUser(toUserId).setText(text);
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONUtil.objToStr(textMessage));
                CHANNEL_GROUP.writeAndFlush(textWebSocketFrame, channel -> Objects.equals(channel.id(), toUserChannelId));
            }
        }
    }


    /**
     * 责任链模式处理消息
     * @param webSocketFrame
     */
    public void dealMessage(Channel channel,TextWebSocketFrame webSocketFrame){
        if(null != webSocketFrame){
            for (IHandleMessage handleMessage : HANDLE_MESSAGE_SET) {
                handleMessage.handleMessage(channel,webSocketFrame);
            }
        }
    }


}
