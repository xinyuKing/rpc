
package com.xinyu.rpc.client.cluster.lb;


import com.xinyu.rpc.annotation.XYrpcLoadBalance;
import com.xinyu.rpc.client.cluster.LoadBalanceStrategy;
import com.xinyu.rpc.provider.ServiceProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@XYrpcLoadBalance(strategy = "polling")
public class PollingLoadBalanceStrategy implements LoadBalanceStrategy {

    private AtomicInteger next = new AtomicInteger(0);

    /**
     * 轮询负载均衡策略
     * @param serviceProviders
     * @return
     */
    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        int length = serviceProviders.size();
        int index = incrementAndGetNext(length);
        return serviceProviders.get(index);
    }

    private int incrementAndGetNext(int length) {
        while (true) {
            int current = next.get();
            int index = (current + 1) % length;
            if (next.compareAndSet(current, index)) {
                return index;
            }
        }
    }
}
