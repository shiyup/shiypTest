package com.syp.test.netty.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author shiyuping
 * @Date 2022/4/7 22:47
 */
public class NioServer {

    public static void main(String[] args) throws IOException {

        /*ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();*/

        //未引入多路复用
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);
        List<SocketChannel> socketChannelList = new ArrayList<>();
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannelList.add(socketChannel);
        }


    }
}
