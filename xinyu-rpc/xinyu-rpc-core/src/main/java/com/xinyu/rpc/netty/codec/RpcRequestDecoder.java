package com.xinyu.rpc.netty.codec;

import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            int length  = msg.readableBytes();
            byte[] bytes = new byte[length];
            msg.readBytes(bytes);
            RpcRequest request = ProtostuffUtil.deserialize(bytes, RpcRequest.class);
            out.add(request);
        } catch (Exception e) {
            log.error("RpcRequestDecoder decode error,msg={}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

}