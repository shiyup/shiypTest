package com.syp.test.netty.demo.chat.server.handler;

import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.handler.bus.AbstractMessageHandler;
import com.syp.test.netty.demo.chat.server.handler.bus.MessageHandlerFactory;
import com.syp.test.netty.demo.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by shiyuping on 2022/4/6 11:12 下午
 *
 * @author shiyuping
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        AbstractMessageHandler messageHandler = MessageHandlerFactory.getMessageHandler(msg.getMessageType());
        if (messageHandler != null) {
            messageHandler.handle(ctx, msg);
        } else {
            //继续传播消息
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //当连接断开时触发 inactive 事件
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经断开", ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常时触发
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经异常断开 异常是{}", ctx.channel(), cause);
    }
}
