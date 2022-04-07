package com.syp.test.netty.demo.chat.message;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
