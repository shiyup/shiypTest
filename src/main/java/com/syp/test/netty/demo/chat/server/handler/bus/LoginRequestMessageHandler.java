package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.LoginRequestMessage;
import com.syp.test.netty.demo.chat.message.LoginResponseMessage;
import com.syp.test.netty.demo.chat.message.Message;
import com.syp.test.netty.demo.chat.server.service.UserServiceFactory;
import com.syp.test.netty.demo.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author shiyuping
 */
@MessageType(Message.LoginRequestMessage)
public class LoginRequestMessageHandler extends AbstractMessageHandler<LoginRequestMessage> {

    @Override
    protected void doHandle(ChannelHandlerContext ctx, LoginRequestMessage msg) {
        String username = msg.getUsername();
        String password = msg.getPassword();
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
