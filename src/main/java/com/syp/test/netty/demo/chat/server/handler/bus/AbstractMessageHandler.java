package com.syp.test.netty.demo.chat.server.handler.bus;

import com.syp.test.netty.demo.chat.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;

/**
 * @Author shiyuping
 * @Date 2022/4/7 10:03
 */
public abstract class AbstractMessageHandler<T extends Message> {

    /*private final TypeParameterMatcher matcher;

    protected AbstractMessageHandler() {
        this.matcher = TypeParameterMatcher.find(this, AbstractMessageHandler.class, "T");;
    }*/

    /**
     * 消息处理
     * @param ctx
     * @param msg
     */
    public void handle(ChannelHandlerContext ctx, Message msg){
        doHandle(ctx, (T)msg);

        /*if (matcher.match(msg)){
            doHandle(ctx,(T)msg);
        }else {
            //继续传播消息
            ctx.fireChannelRead(msg);
        }
*/
    }

    /**
     * 消息处理
     * @param ctx
     * @param msg
     */
    protected abstract void doHandle(ChannelHandlerContext ctx, T msg);
}
