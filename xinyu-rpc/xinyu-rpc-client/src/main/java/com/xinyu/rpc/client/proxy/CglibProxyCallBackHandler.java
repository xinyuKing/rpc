
package com.xinyu.rpc.client.proxy;


import com.xinyu.rpc.client.request.RpcRequestManager;
import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.spring.SpringBeanFactory;
import com.xinyu.rpc.util.RequestIdUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class CglibProxyCallBackHandler implements MethodInterceptor {


    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        // 放过来自Object的相关方法,采用spring的工具类
        if (ReflectionUtils.isObjectMethod(method)) {
            return method.invoke(this,parameters);
        }
        // 进程远程方法调用
        //构建rpc请求,其实就是封装RpcRequest对象
        String requestId = RequestIdUtil.requestId();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        RpcRequest request = RpcRequest.builder()
                .requestId(requestId)
                .className(className)
                .methodName(methodName)
                .parameterTypes(parameterTypes)
                .parameters(parameters)
                .build();
        //发送请求获取响应
        RpcRequestManager requestManager = SpringBeanFactory.getBean(RpcRequestManager.class);
        if (requestManager==null) {
            throw new RuntimeException("no requestManager");
        }

        RpcResponse response = requestManager.sendRequest(request);
        if (response.isError()) {
            throw new RuntimeException(response.getCause());
        }
        return response.getResult();
    }
}
