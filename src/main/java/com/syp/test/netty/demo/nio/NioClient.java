package com.syp.test.netty.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Author shiyuping
 * @Date 2022/4/7 22:49
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
//        try (Socket socket = new Socket("localhost", 8080)) {
//            System.out.println(socket);
//            socket.getOutputStream().write("world".getBytes());
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        System.out.println("waiting...");
        SocketAddress address = sc.getLocalAddress();
        //sc.write(Charset.defaultCharset().encode("hello\nworld\n"));
        sc.write(Charset.defaultCharset().encode("0123\n456789abcdef"));
        sc.write(Charset.defaultCharset().encode("0123456789abcdef3333\n"));
        System.in.read();

    }
}
