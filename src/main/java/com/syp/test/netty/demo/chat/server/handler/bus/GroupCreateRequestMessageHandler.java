package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.GroupCreateRequestMessage;
import com.syp.test.netty.demo.chat.message.GroupCreateResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.session.Group;
import com.syp.test.netty.demo.chat.server.session.GroupSession;
import com.syp.test.netty.demo.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Set;

/**
 * @author shiyuping
 */
@MessageType(Message.GroupCreateRequestMessage)
public class GroupCreateRequestMessageHandler extends AbstractMessageHandler<GroupCreateRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null) {
            // 发生成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入" + groupName));
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }
    }
}
