package com.xinyu.rpc.client.proxy;

import com.xinyu.rpc.cache.ServiceProviderCache;
import com.xinyu.rpc.client.request.RpcRequestManager;
import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.exception.RpcException;
import com.xinyu.rpc.spring.SpringBeanFactory;
import com.xinyu.rpc.util.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class JDKProxyCallBackHandler implements InvocationHandler {

    @Autowired
    private ServiceProviderCache cache;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 放过Object类中的方法
        if(ReflectionUtils.isObjectMethod(method)){
            return method.invoke(this,args);
        }

        // 走网络调用
        // 构造RPC请求
        String requestId = RequestIdUtil.requestId();
        String serviceName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(requestId)
                .className(serviceName)
                .methodName(methodName)
                .parameterTypes(parameterTypes)
                .parameters(args)
                .build();

        // 发送请求获取响应结果
        RpcRequestManager rpcRequestManager = SpringBeanFactory.getBean(RpcRequestManager.class);
        if (rpcRequestManager == null) {
            throw new RpcException("没有RpcRequestManager容器");
        }
        RpcResponse rpcResponse = rpcRequestManager.sendRequest(rpcRequest);
        if (rpcResponse.isError()) {
            throw new RpcException(rpcResponse.getCause());
        }
        return rpcResponse.getResult();
    }

}
