package com.syp.test.netty.demo.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by shiyuping on 2022/3/20 5:21 下午
 * @author shiyuping
 */
@Slf4j
public class ReconnectClient {

    private String host;
    private int port;
    private Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        ReconnectClient client = new ReconnectClient("127.0.0.1",8080);
        client.doConnect();
    }

    public ReconnectClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                // 选择客户 Socket 实现类，NioSocketChannel 表示基于 NIO 的客户端实现
                .channel(NioSocketChannel.class)
                // ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler());
                        // 消息会经过通道 handler 处理，这里是将 String => ByteBuf 编码发出
                        channel.pipeline().addLast(new StringEncoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("与服务端断开了连接,3s后重连");
                                ctx.channel().eventLoop().schedule(() -> {
                                    try {
                                        doConnect();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }, 3L, TimeUnit.SECONDS);
                            }
                        });
                    }
                });

    }

    private void doConnect() throws InterruptedException {
        ChannelFuture f = bootstrap.connect(host, port);
        f.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.debug("连接成功");
                // 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
                // 写入消息并清空缓冲区
                channelFuture.channel().writeAndFlush("hello world");
            } else {
                log.debug("未连接成功，3秒后进行重连");
                channelFuture.channel().eventLoop().schedule(() -> {
                    try {
                        doConnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 3L, TimeUnit.SECONDS);
            }
        });
        //阻塞等待channel关闭
        f.channel().closeFuture().sync();
    }
}
