package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.ChatRequestMessage;
import com.syp.test.netty.demo.chat.message.ChatResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shiyuping
 */
@MessageType(Message.ChatRequestMessage)
public class ChatRequestMessageHandler extends AbstractMessageHandler<ChatRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, ChatRequestMessage msg) {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        // 在线
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
        // 不在线
        else {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
        }
    }
}
