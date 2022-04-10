package com.syp.test.netty.demo.nio;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * 未引入多路复用
 * @Author shiyuping
 * @Date 2022/4/7 22:47
 */
@Slf4j
public class NioServer {

    public static void main(String[] args) throws IOException {

        /*ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();*/


        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //非阻塞模式
        ssc.configureBlocking(false);
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            log.debug("connecting...");
            // 阻塞模式下阻塞方法，线程停止运行
            // 非阻塞模式下会返回 null，继续运行
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected... {}", sc);
                sc.configureBlocking(false); // 非阻塞模式
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 5. 接收客户端发送的数据
                log.debug("before read... {}", channel);
                // 阻塞模式下阻塞方法，线程停止运行
                // 非阻塞模式下在没有数据可读时，会返回 0
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    //debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", channel);
                }
            }
        }
        //非阻塞模式下，即使没有连接建立，和可读数据，线程仍然在不断运行，白白浪费了 cpu

    }

}
