package com.syp.test.netty.demo.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Created by shiyuping on 2022/3/20 5:21 下午
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
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
                        }
                    });
            // 指定要连接的服务器和端口
            // Netty 中很多方法都是异步的，如 connect-nio线程
            // 这时需要使用 sync 方法等待 connect 建立连接完毕
            ChannelFuture f = bootstrap.connect("127.0.0.1", 8080).sync();
            // 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
            // 写入消息并清空缓冲区
            f.channel().writeAndFlush("hello world");
            //阻塞等待channel关闭
            f.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
