package com.syp.test.netty.demo.chat.server.handler.bus;

import cn.hutool.core.collection.CollectionUtil;
import com.syp.test.netty.demo.chat.message.GroupChatRequestMessage;
import com.syp.test.netty.demo.chat.message.GroupChatResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.GroupSession;
import com.syp.test.netty.demo.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Set;


/**
 * @author shiyuping
 */
@MessageType(Message.GroupChatRequestMessage)
public class GroupChatRequestMessageHandler extends AbstractMessageHandler<GroupChatRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, GroupChatRequestMessage msg) {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Set<String> members = groupSession.getMembers(msg.getGroupName());
        if (CollectionUtil.isEmpty(members)){
            ctx.writeAndFlush(new GroupChatResponseMessage(false, msg.getGroupName() + "群组不存在"));
        }
        List<Channel> channels = groupSession.getMembersChannel(msg.getGroupName());
        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
