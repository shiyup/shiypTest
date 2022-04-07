package com.syp.test.netty.demo.rpc;

import lombok.Data;

/**
 * Created by shiyuping on 2022/4/6 11:40 下午
 */
@Data
public class MsgProtocol<T> {

    private MsgHeader header; // 协议头

    private T body; // 协议体

}
