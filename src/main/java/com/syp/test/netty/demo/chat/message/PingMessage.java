package com.syp.test.netty.demo.chat.message;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PingMessage extends Message {

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
