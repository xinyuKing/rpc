package com.xinyu.rpc.netty.codec;

import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcRequestEncoder extends MessageToMessageEncoder<RpcRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest RpcRequest, List<Object> out) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(RpcRequest);
            ByteBuf buffer = ctx.alloc().buffer(bytes.length);
            buffer.writeBytes(bytes);
            out.add(buffer);
        } catch (Exception e) {
            log.error("RpcRequestEncoder encode error,msg={}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
