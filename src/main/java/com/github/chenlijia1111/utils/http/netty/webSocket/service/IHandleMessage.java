package com.github.chenlijia1111.utils.http.netty.webSocket.service;

import com.github.chenlijia1111.utils.http.netty.webSocket.pojo.AbstractMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


/**
 * 处理消息接口
 * 使用责任链模式处理，方便扩展功能
 *
 * 默认的消息处理 只处理单对单的消息，群组消息转发需要调用者自己实现，
 * 因为在内存中存太多数据不好
 * 另外写处理器，判断这个消息是否已送达，并把消息数据存到数据库
 * 以及掉线消息的处理都需要调用者另外实现，需要数据库支持，
 * 把没在线的消息先存起来，等到上线了再给他发过去
 *
 * 首先，调用者需要创建一个实现类，保存每次发送的消息
 * 然后当有用户上线的时候，
 * 根据 {@link AbstractMessage#getSendStatus()} 判断是否发送过，把没发送的消息都给发过去
 * @see HandleTextMessage
 * @see HandleAudioMessage
 * @see HandleVideoMessage
 *
 * @author 陈礼佳
 * @since 2020/4/5 8:37
 */
public interface IHandleMessage {

    /**
     * 处理消息方法
     * 子类根据消息类型来处理不同的消息
     *
     * @param webSocketFrame
     */
    void handleMessage(Channel channel, TextWebSocketFrame webSocketFrame);

}
