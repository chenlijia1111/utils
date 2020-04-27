package com.github.chenlijia1111.utils.http.netty.webSocket;

import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.http.netty.webSocket.handle.NioWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;

/**
 * netty webSocket服务
 * 利用netty 的nio特性搭建高可用的websocket服务
 * 需要异步启动，不然会阻塞当前调用的主线程
 * @author 陈礼佳
 * @since 2020/4/4 11:48
 */
public class NettyWebSocketServer extends Thread {

    private static final Logger log = new LogUtil(NettyWebSocketServer.class);

    //netty端口
    private int port;

    public NettyWebSocketServer(int port) {
        this.port = port;
    }

    /**
     * 启动方法
     */
    @Override
    public void run(){
        log.info("netty webSocket 启动，端口：" + port);
        //用于接收请求
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //用于处理请求
        NioEventLoopGroup worker = new NioEventLoopGroup();

        //启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker);
        //管道
        bootstrap.channel(NioServerSocketChannel.class);
        //child Handle 处理方法
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
                socketChannel.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
                socketChannel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
                socketChannel.pipeline().addLast("handler",new NioWebSocketHandler());//自定义的业务handler
            }
        });
        try {
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("netty webSocket 启动成功");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("netty webSocket 已关闭");
        }
    }

}
