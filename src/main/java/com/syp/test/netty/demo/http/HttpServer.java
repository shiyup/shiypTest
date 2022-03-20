package com.syp.test.netty.demo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * 手写http服务
 * Created by shiyuping on 2022/1/16 1:38 下午
 */
@Slf4j
public class HttpServer {


    public void start(int port) throws Exception {

        //Reactor单线程模型
        // EventLoopGroup group = new NioEventLoopGroup(1);
        //Reactor多线程模型--默认会启动 2 倍 CPU 核数的线程
        // EventLoopGroup group = new NioEventLoopGroup();
        //Reactor主从线程模型
        //主 Reactor
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //从 Reactor
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //首先创建启动器，负责装配netty组件，启动服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //配置线程池
            serverBootstrap.group(bossGroup, workerGroup)
                    //--------------Channel 初始化----------------
                    //设置 Channel 类型---------按需切换，例如 OioServerSocketChannel、EpollServerSocketChannel 等。
                    .channel(NioServerSocketChannel.class)
                    //注册 ChannelHandler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    // HTTP 编解码
                                    .addLast("codec", new HttpServerCodec())
                                    // HttpContent 压缩
                                    .addLast("compressor", new HttpContentCompressor())
                                    // HTTP 消息聚合
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    // 自定义业务逻辑处理器
                                    .addLast("handler", new HttpServerHandler())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 连接建立时会执行该方法
                                    log.debug("connected {}", ctx.channel());
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    // 连接断开时会执行该方法
                                    log.debug("disconnect {}", ctx.channel());
                                    super.channelInactive(ctx);
                                }
                            });;

                        }
                    })
                    //设置 Channel 参数
                    //ServerBootstrap 设置 Channel 属性有option和childOption两个方法，
                    //option 主要负责设置 Boss 线程组，而 childOption 对应的是 Worker 线程组。
                    //SO_KEEPALIVE设置为 true 代表启用了 TCP SO_KEEPALIVE 属性，TCP 会主动探测连接状态，即连接保活
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //端口绑定
            //bind() 方法会真正触发启动，sync() 方法则会阻塞，直至整个启动过程完成
            ChannelFuture f = serverBootstrap.bind(port).sync();
            System.out.println("Http Server started， Listening on " + port);
            // 关闭channel
            f.channel().closeFuture().sync();

        } finally {
            //优雅关闭
            // 该方法会首先切换 EventLoopGroup 到关闭状态从而拒绝新的任务的加入，
            // 然后在任务队列的任务都处理完成后，停止线程的运行。
            // 从而确保整体应用是在正常有序的状态下退出的
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.debug("stopped");
        }

    }

    public static void main(String[] args) throws Exception {

        new HttpServer().start(8088);

    }

}
