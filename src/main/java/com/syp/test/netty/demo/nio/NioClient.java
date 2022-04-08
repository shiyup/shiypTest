package com.syp.test.netty.demo.nio;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author shiyuping
 * @Date 2022/4/7 22:49
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

    }
}
