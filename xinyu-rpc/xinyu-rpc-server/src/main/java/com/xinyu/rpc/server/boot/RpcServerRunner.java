package com.xinyu.rpc.server.boot;

import com.xinyu.rpc.server.registry.RpcRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcServerRunner {

    @Autowired
    private RpcRegistry rpcRegistry;

    @Autowired
    private RpcServer rpcServer;

    /**
     * 1.完成服务注册
     *      思路分析:
     *          1.找到业务代码需要向外部暴露那些接口
     *              自定义注解
     *          2.人后基于你的注册中心sdk将相关信息写入到注册中心
     *              zookeeper
     *                  curator,ZkClient,……
     * 2.基于netty编写一个服务器程序nettyServer
     *      请求解码器：1次解码（TCP),2次解码（反序列化）
     *      请求处理器：根据反序列化的结果【拿到了请求的参数】调用具体的业务接口方法实现,完成封装响应
     *      响应的编码: 2次编码（序列化），1次编码
     */
    public void run() {
        // 服务注册
        rpcRegistry.serviceRegistry();
        // 开启netty服务
        rpcServer.start();
    }
}
