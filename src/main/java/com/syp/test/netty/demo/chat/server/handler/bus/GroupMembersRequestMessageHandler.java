package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.GroupMembersRequestMessage;
import com.syp.test.netty.demo.chat.message.GroupMembersResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

/**
 * @author shiyuping
 */
@MessageType(Message.GroupMembersRequestMessage)
public class GroupMembersRequestMessageHandler extends AbstractMessageHandler<GroupMembersRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) {
        Set<String> members = GroupSessionFactory.getGroupSession()
                .getMembers(msg.getGroupName());
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));
    }
}
