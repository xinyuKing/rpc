
package com.xinyu.rpc.netty.handler;

import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.spring.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        log.info("rpc服务端收到的请求: {}", rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        // 请求中有什么数据
        String interfaceName = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();
        try {
            Object target = SpringBeanFactory.getBean(Class.forName(interfaceName));
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(target, parameters);
            rpcResponse.setResult(result);
        } catch (Exception e) {
            log.error("RpcRequestHandler invoke error: msg={}", e.getMessage());
            rpcResponse.setCause(e);
        } finally {
            // 返回响应
            log.info("rpc服务端执行成功，返回的响应: {}",rpcResponse);
            channelHandlerContext.channel().writeAndFlush(rpcResponse);
        }
    }
}
