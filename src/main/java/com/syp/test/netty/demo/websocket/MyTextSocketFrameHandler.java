package com.syp.test.netty.demo.websocket;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author shiyuping
 * @Date 2022/4/11 17:55
 */
public class MyTextSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup defaultEventLoopGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"发送了"+msg);
        defaultEventLoopGroup.forEach(ch->{
                    SocketAddress socketAddress = ctx.channel().remoteAddress();
                    byte[] bytes = socketAddress.toString().getBytes();
                    String s = Arrays.toString(bytes);
                    ch.writeAndFlush(new TextWebSocketFrame(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + " " + msg.text()));
                }
        );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
        defaultEventLoopGroup.add(ctx.channel());
        defaultEventLoopGroup.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"加入群聊"));
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
        defaultEventLoopGroup.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"t退出群聊"));

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close(); //关闭连接
    }
}
