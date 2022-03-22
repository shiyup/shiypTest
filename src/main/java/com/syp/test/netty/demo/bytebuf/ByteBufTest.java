package com.syp.test.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * Created by shiyuping on 2022/3/22 10:05 下午
 */
public class ByteBufTest {
    public static void main(String[] args) {
        //创建了一个默认的 ByteBuf（池化基于直接内存的 ByteBuf）
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        log(buffer);
        buffer.writeByte(1);
        log(buffer);
        //创建池化基于堆的 ByteBuf
        ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer(10);
        log(heapBuffer);
        //创建池化基于直接内存的 ByteBuf
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer(10);
    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }


}
