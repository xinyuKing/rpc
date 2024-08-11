package com.xinyu.rpc.netty.codec;

import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class RpcResponseDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            int length  = byteBuf.readableBytes();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            RpcResponse response = ProtostuffUtil.deserialize(bytes, RpcResponse.class);
            list.add(response);
        } catch (Exception e) {
            // 捕获并记录解码过程中可能出现的异常
            log.error("RpcRequestDecoder decode error,message={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
