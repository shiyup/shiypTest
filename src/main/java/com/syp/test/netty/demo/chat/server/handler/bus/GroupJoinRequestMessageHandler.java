package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.GroupJoinRequestMessage;
import com.syp.test.netty.demo.chat.message.GroupJoinResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.Group;
import com.syp.test.netty.demo.chat.server.session.GroupSession;
import com.syp.test.netty.demo.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shiyuping
 */
@MessageType(Message.GroupJoinRequestMessage)
public class GroupJoinRequestMessageHandler extends AbstractMessageHandler<GroupJoinRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        if (groupSession.getMembers(msg.getGroupName()).contains(msg.getUsername())){
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getUsername() + "已在群组里"));
            return;
        }
        Group group = groupSession.joinMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群加入成功"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
