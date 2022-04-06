package com.syp.test.netty.demo.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

import static jdk.nashorn.internal.objects.NativeFunction.bind;

/**
 * Created by shiyuping on 2022/3/20 5:20 下午
 */
public class HelloServer {
    public static void main(String[] args) throws InterruptedException {
        //Reactor主从线程模型
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 1、启动器，负责装配netty组件，启动服务器
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    // 2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
                    .group(bossGroup, workerGroup)
                    // 3、选择服务器的 ServerSocketChannel 实现
                    .channel(NioServerSocketChannel.class)
                    // 4、child 负责处理读写，该方法决定了 child 执行哪些操作
                    // ChannelInitializer 处理器（仅执行一次）
                    // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new LoggingHandler());
                            // 5、SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
                            nioSocketChannel.pipeline().addLast(new StringDecoder());
                            // 6、SocketChannel的业务处理，使用上一个处理器的处理结果
                            nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
                                    //ctx.alloc().buffer();
                                    System.out.println(s);
                                }
                            });

                        }

                    });
            // 7、ServerSocketChannel绑定8080端口
            //bind() 方法会真正触发启动，sync() 方法则会阻塞，直至整个启动过程完成
            ChannelFuture f = serverBootstrap.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
