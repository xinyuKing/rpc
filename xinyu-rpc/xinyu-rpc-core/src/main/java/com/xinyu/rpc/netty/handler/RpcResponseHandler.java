package com.xinyu.rpc.netty.handler;

import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.netty.request.RequestPromise;
import com.xinyu.rpc.netty.request.RpcRequestHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        log.info("rpc调用成功,响应结果为:{}",response);
        RequestPromise promise = RpcRequestHolder.getRequestPromise(response.getRequestId());
        if (promise!=null) {
            // 结束阻塞,回调监听
            promise.setSuccess(response);
        }
    }
}
