package com.syp.test.netty.demo.rpc;

import lombok.Data;

/**
 * Created by shiyuping on 2022/4/6 11:34 下午
 */
@Data
public class MsgHeader {
    private int magic; // 魔数

    private byte version; // 协议版本号

    private byte serializerAlgorithm; // 序列化算法

    private byte msgType; // 报文类型

    private byte status; // 状态

    private int sequenceId; // 请求序号

    private int msgLength; // 数据长度
}
