
package com.xinyu.rpc.client.cluster.lb;

import com.xinyu.rpc.annotation.XYrpcLoadBalance;
import com.xinyu.rpc.client.cluster.LoadBalanceStrategy;
import com.xinyu.rpc.provider.ServiceProvider;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

@XYrpcLoadBalance(strategy = "random")
public class RandomLoadBalanceStrategy implements LoadBalanceStrategy {
    /**
     * 随机选择一个服务提供者
     * @param serviceProviders
     * @return
     */
    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        int length = serviceProviders.size();
        int index = RandomUtils.nextInt(0, length);
        return serviceProviders.get(index);
    }
}
