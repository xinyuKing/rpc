package com.xinyu.rpc.netty.codec;

import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrameEncoder extends LengthFieldPrepender {
    public FrameEncoder() {
        super(4);
        log.info("FrameEncoder init");
    }
}
