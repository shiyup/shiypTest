package com.syp.test.netty.demo.chat.client;

import com.syp.test.netty.demo.chat.message.*;
import com.syp.test.netty.demo.chat.protocol.MessageCodecSharable;
import com.syp.test.netty.demo.chat.protocol.ProtocolFrameDecoder;
import com.syp.test.netty.demo.chat.client.handler.ClientHeartbeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shiyuping
 */
@Slf4j
public class ChatClient {

    private final String host;
    private final int port;
    private Bootstrap bootstrap;
    private Channel channel;

    private final MessageCodecSharable messageCodec;
    private final Scanner scanner;
    private final CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
    private final AtomicBoolean EXIT = new AtomicBoolean(false);




    public static void main(String[] args){
        ChatClient client = new ChatClient("127.0.0.1",8080);
        client.doConnect();
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.messageCodec = new MessageCodecSharable();
        this.scanner = new Scanner(System.in);
        initBootstrap();
    }

    private void initBootstrap() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(messageCodec);
                // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                // 3s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
                ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                // 客户端心跳处理-每隔3s发送一个心跳包
                ch.pipeline().addLast(new ClientHeartbeatHandler());
                ch.pipeline().addLast("clientHandler", new ChannelInboundHandlerAdapter() {
                    // 接收响应消息
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("msg: {}", msg);
                        //模拟登陆
                        if ((msg instanceof LoginResponseMessage)) {
                            LoginResponseMessage response = (LoginResponseMessage) msg;
                            if (response.isSuccess()) {
                                System.out.println("登陆成功");
                                // 唤醒 system in 线程
                                WAIT_FOR_LOGIN.countDown();
                            }else {
                                System.out.println("用户名密码错误");
                                //登陆失败--退出
                                ctx.channel().close();
                            }

                        }
                    }

                    // 在连接建立后触发 active 事件
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        log.debug("连接建立...{}",ctx.channel());
                        // 负责接收用户在控制台的输入，负责向服务器发送各种消息
                        new Thread(() -> {
                            consoleInputAndSendMsg(ctx);
                        }, "system in").start();
                    }

                    // 在连接断开时触发
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        if (EXIT.get()){
                            log.debug("连接已经断开，按任意键退出...");
                            group.shutdownGracefully();
                        }else {
                            //非正常退出时断线重连
                            log.debug("连接已经断开，立即重连....");
                            doConnect();
                        }
                    }

                    // 在出现异常时触发
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        log.debug("出现异常，连接已经断开，立即重连..{}", cause.getMessage());
                        doConnect();
                    }
                });
            }
        });
    }

    private void consoleInputAndSendMsg(ChannelHandlerContext ctx) {
            System.out.println("请输入用户名:");
            String username = scanner.nextLine();
            if(EXIT.get()){
                return;
            }
            System.out.println("请输入密码:");
            String password = scanner.nextLine();
            if(EXIT.get()){
                return;
            }
            // 构造消息对象
            LoginRequestMessage message = new LoginRequestMessage(username, password);
            // 发送消息
            ctx.writeAndFlush(message);
            try {
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                System.out.println("==================================");
                System.out.println("send [username] [content]");
                System.out.println("gsend [group name] [content]");
                System.out.println("gcreate [group name] [m1,m2,m3...]");
                System.out.println("gmembers [group name]");
                System.out.println("gjoin [group name]");
                System.out.println("gquit [group name]");
                System.out.println("quit");
                System.out.println("==================================");
                String command = null;
                try {
                    command = scanner.nextLine();
                } catch (Exception e) {
                    break;
                }
                if(EXIT.get()){
                    return;
                }
                String[] s = command.split(" ");
                switch (s[0]){
                    case "send":
                        ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gsend":
                        ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gcreate":
                        Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                        // 加入自己
                        set.add(username);
                        ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                        break;
                    case "gmembers":
                        ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                        break;
                    case "gjoin":
                        ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                        break;
                    case "gquit":
                        ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                        break;
                    case "quit":
                        EXIT.set(true);
                        ctx.channel().close();
                        return;
                    default:
                        System.out.println("unexpected command:"+ s[0]);
                        break;
                }
            }
    }

    public void doConnect(){
        if (channel != null && channel.isActive()) {
            return;
        }
        try {
            ChannelFuture f = bootstrap.connect(host, port);
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    channel = channelFuture.channel();
                    log.debug("连接成功");
                } else {
                    log.debug("未连接成功，5秒后进行重连");
                    channelFuture.channel().eventLoop().schedule(this::doConnect, 5L, TimeUnit.SECONDS);
                }
            });
            //阻塞等待channel关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
