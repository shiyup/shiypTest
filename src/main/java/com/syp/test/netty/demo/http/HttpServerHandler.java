package com.syp.test.netty.demo.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Created by shiyuping on 2022/1/16 1:46 下午
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        String content = String.format("Receive http request, uri: %s, method: %s, content: %s%n", fullHttpRequest.uri(), fullHttpRequest.method(), fullHttpRequest.content().toString(CharsetUtil.UTF_8));

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(content.getBytes()));

        ctx.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
