
package com.xinyu.rpc.client.boot;

import com.xinyu.rpc.client.discovery.RpcServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RpcClientRunner {

    @Autowired
    private RpcServiceDiscovery discovery;

    /**
     * 1,服务发现
     *      从注册中心中获取注册的数据，并存入本地缓存
     *          接口->providers
     *      将数据存入缓存并监听接口的变化->一旦有变更，则更新缓存
     *  2, 基于用户使用的接口生成代理
     *     -> 基于 spring 的 BeanPostProcessor 【后置处理器完成】
     *     （jdk,cglib）
     *  3,在代理实现中封装底层的网络通信的逻辑
     *      封装请求
     *      基于接口拿到提供者信息建立链接
     *      基于链接发送请求【对请求进行编码,序列化】
     *      获取响应【对响应进行解码】
     */
    public void run() {
        discovery.serviceDiscovery();
    }
}
