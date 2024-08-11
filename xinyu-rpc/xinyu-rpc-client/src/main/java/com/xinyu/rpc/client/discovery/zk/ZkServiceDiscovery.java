
package com.xinyu.rpc.client.discovery.zk;

import com.xinyu.rpc.client.discovery.RpcServiceDiscovery;
import com.xinyu.rpc.cache.ServiceProviderCache;
import com.xinyu.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class ZkServiceDiscovery implements RpcServiceDiscovery {

    @Autowired
    private ClientZKit zKit;

    @Autowired
    private ServiceProviderCache cache;

    @Override
    public void serviceDiscovery() {
        // 获取所有接口列表
        List<String> serviceList = zKit.getServiceList();
        if(!serviceList.isEmpty()){
            for(String serviceName:serviceList){
                // 获取该接口所有提供者的接口
                List<ServiceProvider> serviceInfos = zKit.getServiceInfos(serviceName);
                cache.put(serviceName,serviceInfos);
                log.info("订阅的接口{}，提供者有{}",serviceName,serviceInfos);
                zKit.subscribeZKEvent(serviceName);
            }
        }
    }
}
