
package com.xinyu.rpc.client.cluster;


import com.xinyu.rpc.annotation.XYrpcLoadBalance;
import com.xinyu.rpc.client.config.RpcClientConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultStrategyProvider implements StrategyProvider, ApplicationContextAware {

    @Autowired
    private RpcClientConfiguration rpcClientConfiguration;

    private LoadBalanceStrategy loadBalanceStrategy;

    @Override
    public LoadBalanceStrategy getStrategy() {
        return loadBalanceStrategy;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(XYrpcLoadBalance.class);
        for (Object bean : beansWithAnnotation.values()) {
            XYrpcLoadBalance annotation = bean.getClass().getAnnotation(XYrpcLoadBalance.class);
            if(annotation.strategy().equalsIgnoreCase(rpcClientConfiguration.getRpcClientClusterStrategy())) {
                loadBalanceStrategy = (LoadBalanceStrategy) bean;
                break;
            }
        }
    }
}
