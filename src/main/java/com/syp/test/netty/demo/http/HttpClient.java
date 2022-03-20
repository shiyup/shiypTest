package com.syp.test.netty.demo.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 手写http客户端
 * Created by shiyuping on 2022/1/16 2:30 下午
 */
public class HttpClient {
    public void connect(String host, int port) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap b = new Bootstrap();
            //配置线程模型
            b.group(group);
            //注册channel--NioSocketChannel 表示基于 NIO 的客户端实现
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            //注册 ChannelHandler
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {

                    ch.pipeline()
                            //http解码
                            .addLast(new HttpResponseDecoder())
                            //http编码
                            .addLast(new HttpRequestEncoder())
                            //自定义处理器
                            .addLast(new HttpClientHandler());

                }

            });

            ChannelFuture f = b.connect(host, port)
                    // Netty 中很多方法都是异步的，如 connect
                    // 这时需要使用 sync 方法等待 connect 建立连接完毕
                    .sync();

            URI uri = new URI("http://127.0.0.1:8088");

            String content = "hello world";

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));

            request.headers()
                    .set(HttpHeaderNames.HOST, host)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            // 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
            // 写入消息并清空缓冲区
            f.channel().write(request);
            f.channel().flush();
            //
            f.channel().closeFuture().sync();

        } finally {

            group.shutdownGracefully();

        }

    }

    public static void main(String[] args) throws Exception {

        HttpClient client = new HttpClient();

        client.connect("127.0.0.1", 8088);

    }
}
