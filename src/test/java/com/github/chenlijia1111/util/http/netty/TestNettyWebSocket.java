package com.github.chenlijia1111.util.http.netty;

import com.github.chenlijia1111.utils.http.netty.webSocket.NettyWebSocketServer;
import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2020/4/4 12:10
 */
public class TestNettyWebSocket {

    @Test
    public void test1(){
        new NettyWebSocketServer(8090).start();
    }

}
