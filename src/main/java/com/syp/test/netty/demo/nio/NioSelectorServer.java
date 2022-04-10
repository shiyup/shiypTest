package com.syp.test.netty.demo.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by shiyuping on 2022/4/9 9:17 上午
 * 线程可以配合 Selector 完成对多个 Channel 可读写事件的监控，这称之为多路复用
 *
 * * 多路复用仅针对网络 IO、普通文件 IO 没法利用多路复用
 * * 如果不用 Selector 的非阻塞模式，线程大部分时间都在做无用功，而 Selector 能够保证
 *   * 有可连接事件时才去连接
 *   * 有可读事件才去读取
 *   * 有可写事件才去写入
 *     * 限于网络传输能力，Channel 未必时时可写，一旦 Channel 可写，会触发 Selector 的可写事件
 *
 *
 *     好处
 * * 一个线程配合 selector 就可以监控多个 channel 的事件，事件发生线程才去处理。避免非阻塞模式下所做无用功
 * * 让这个线程能够被充分利用
 * * 节约了线程的数量
 * * 减少了线程上下文切换
 */
@Slf4j
public class NioSelectorServer {

    public static void main(String[] args) throws IOException {

        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //非阻塞模式
        ssc.configureBlocking(false);
        //创建selector
        Selector selector = Selector.open();
        //绑定 Channel 感兴趣事件
        //SelectionKey就是将来事件发生时，可以知道什么事件哪个channel发生的
        SelectionKey selectionkey =ssc.register(selector, 0,null);
        selectionkey.interestOps(SelectionKey.OP_ACCEPT);
        // 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            //select()没有事件发生回阻塞，
            selector.select();
            // 获取所有事件
            Set<SelectionKey> keys = selector.selectedKeys();
            // 遍历所有事件，逐一处理
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 判断事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel c = (ServerSocketChannel) key.channel();
                    // 必须处理
                    SocketChannel sc = c.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment
                    // 将一个 byteBuffer 作为附件关联到 selectionKey 上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                    log.debug("scKey:{}", scKey);
                }else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel(); // 拿到触发事件的channel
                        // 获取 selectionKey 上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer); // 如果是正常断开，read 的方法的返回值是 -1
                        if(read == -1) {
                            key.cancel();
                        } else {
                            //分隔符处理消息边界
                            split(buffer);
                            // 需要扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer); // 0123456789abcdef3333\n
                                key.attach(newBuffer);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();  // 因为客户端断开了,因此需要将 key 取消（从 selector 的 keys 集合中真正删除 key）
                    }
                }
                // 处理完毕，必须将事件移除
                // 事件发生后，要么处理，要么取消（cancel），不能什么都不做，否则下次该事件仍会触发，这是因为 nio 底层使用的是水平触发
                iter.remove();
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                // 把这条完整消息存入新的 ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从 source 读，向 target 写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                //debugAll(target);
            }
        }
        source.compact(); // 0123456789abcdef  position 16 limit 16
    }
}
