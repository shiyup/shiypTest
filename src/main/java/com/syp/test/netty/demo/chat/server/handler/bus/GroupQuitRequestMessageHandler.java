package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.GroupJoinResponseMessage;
import com.syp.test.netty.demo.chat.message.GroupQuitRequestMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.Group;
import com.syp.test.netty.demo.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author shiyuping
 */
@MessageType(Message.GroupQuitRequestMessage)
public class GroupQuitRequestMessageHandler extends AbstractMessageHandler<GroupQuitRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "已退出群" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
