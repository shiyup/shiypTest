package com.syp.test.netty.demo.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author shiyuping
 * @Date 2022/4/11 17:51
 */
public class WebSocketServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup);
            //serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            //serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec());  //因为基于Http协议，使用http的编码和解码器
                    //因为http通信事以块方式写，添加ChunkedWriteHandler处理器
                    pipeline.addLast(new ChunkedWriteHandler());

                    /*
                     * http的数据在传输过程中分段，httpObjectAggregator就是可以将多个段聚合
                     * /**
                     * Creates a new instance.
                     * @param maxContentLength the maximum length of the aggregated content in bytes.
                     * If the length of the aggregated content exceeds this value,
                     * {@link #handleOversizedMessage(ChannelHandlerContext, HttpMessage)} will be called.
                     *参数maxContentLength表示聚合内容的最大长度（字节）。
                     */

                    pipeline.addLast(new HttpObjectAggregator(9090));

                    //说明：1 对应websocket，他的数据是以帧（frame）形式传递
                    //2：可以看到webSocketFrame下面有六个子类
                    //3：浏览器请求时 ws：//localhost：8088/netty 表示请求的uri
                    //4： WebSocketClientProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                    pipeline.addLast(new WebSocketServerProtocolHandler("/netty"));
                    pipeline.addLast(new MyTextSocketFrameHandler());

                }
            });

            ChannelFuture sync = serverBootstrap.bind(8088).sync();
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
