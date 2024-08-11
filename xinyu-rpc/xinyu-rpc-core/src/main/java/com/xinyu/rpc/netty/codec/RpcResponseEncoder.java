package com.xinyu.rpc.netty.codec;

import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcResponseEncoder extends MessageToMessageEncoder<RpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse, List<Object> list) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(rpcResponse);
            ByteBuf buf = channelHandlerContext.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            list.add(buf);
        } catch (Exception e) {
            log.error("RpcResponseEncoder encode error,message={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
