package com.syp.test.netty.demo.chat.server.handler;

import com.syp.test.netty.demo.chat.message.LoginRequestMessage;
import com.syp.test.netty.demo.chat.message.LoginResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.protocol.MsgProtocol;
import com.syp.test.netty.demo.chat.server.service.UserServiceFactory;
import com.syp.test.netty.demo.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by shiyuping on 2022/4/6 11:12 下午
 */
@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<MsgProtocol<Message>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol<Message> msg) throws Exception {
        byte msgType = msg.getHeader().getMsgType();

        Message msgBody = msg.getBody();

        if (msgBody instanceof LoginRequestMessage){
            LoginRequestMessage loginRequestMessage = (LoginRequestMessage)msgBody;
            String username = loginRequestMessage.getUsername();
            String password = loginRequestMessage.getPassword();
            boolean login = UserServiceFactory.getUserService().login(username, password);
            LoginResponseMessage message;
            if (login) {
                SessionFactory.getSession().bind(ctx.channel(), username);
                message = new LoginResponseMessage(true, "登录成功");
            } else {
                message = new LoginResponseMessage(false, "用户名或密码不正确");
            }
            ctx.writeAndFlush(message);
        }
    }
}
