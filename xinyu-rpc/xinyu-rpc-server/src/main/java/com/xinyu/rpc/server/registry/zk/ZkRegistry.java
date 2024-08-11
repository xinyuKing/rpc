package com.xinyu.rpc.server.registry.zk;

import com.xinyu.rpc.server.config.RpcServerConfiguration;
import com.xinyu.rpc.server.registry.RpcRegistry;
import com.xinyu.rpc.annotation.XYrpcService;

import com.xinyu.rpc.spring.SpringBeanFactory;
import com.xinyu.rpc.util.IpUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@DependsOn("springBeanFactory")
@Slf4j
public class ZkRegistry implements RpcRegistry {

    @Autowired
    private ServerZKit serverZKit;


    @Autowired
    private RpcServerConfiguration rpcServerConfiguration;


    /**
     * 找到需要注册哪些接口,通过自定义注解
     */
    @Override
    public void serviceRegistry() {
        Map<String, Object> beanListByAnnotationClass = SpringBeanFactory.getBeanListByAnnotationClass(XYrpcService.class);
        if(beanListByAnnotationClass!=null && !beanListByAnnotationClass.isEmpty()){
            // 创建根节点
            serverZKit.createRootNode();
            // 获取ip
            String ip = IpUtil.getRealIp();

            // 遍历标注了HrpcService注解的所有bean--------》找要暴露的接口信息
            for (Object bean : beanListByAnnotationClass.values()) {
                XYrpcService xyrpcService = bean.getClass().getAnnotation(XYrpcService.class);
                Class<?> interfaceClass = xyrpcService.interfaceClass();
                // 拿到接口名称
                String interfaceName = interfaceClass.getName();
                // 创建代表接口的持久子节点
                serverZKit.createPersistentNode(interfaceName);
                // 创建代表当前提供者的临时子节点【在接口下面】
                // String providerNode=ip+":"+rpcServerConfiguration.getServerPort();
                String providerNode=ip+":"+rpcServerConfiguration.getRpcPort();
                serverZKit.createNode(interfaceName+"/"+providerNode);
                log.info("{}---{}服务注册成功!",interfaceName,providerNode);
            }
        }
    }



}
