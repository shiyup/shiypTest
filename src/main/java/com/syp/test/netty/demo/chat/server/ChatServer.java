package com.syp.test.netty.demo.chat.server;

import com.syp.test.netty.demo.chat.protocol.MessageCodecSharable;
import com.syp.test.netty.demo.chat.protocol.ProtocolFrameDecoder;
import com.syp.test.netty.demo.chat.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
        QuitHandler QUIT_HANDLER = new QuitHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtocolFrameDecoder())
                                    .addLast(LOGGING_HANDLER)
                                    .addLast(MESSAGE_CODEC)
                                    // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                                    // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                                    .addLast(new IdleStateHandler(5, 0, 0))
                                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
                                    .addLast(new ChannelDuplexHandler() {
                                        // 用来触发特殊事件
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            // 触发了读空闲事件
                                            if (event.state() == IdleState.READER_IDLE) {
                                                log.debug("已经 5s 没有读到数据了, 断开与client的连接");
                                                ctx.channel().close();
                                            }
                                        }
                                    })
                                    .addLast(LOGIN_HANDLER)
                                    .addLast(CHAT_HANDLER)
                                    .addLast(GROUP_CREATE_HANDLER)
                                    .addLast(GROUP_JOIN_HANDLER)
                                    .addLast(GROUP_MEMBERS_HANDLER)
                                    .addLast(GROUP_QUIT_HANDLER)
                                    .addLast(GROUP_CHAT_HANDLER)
                                    .addLast(QUIT_HANDLER);
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
