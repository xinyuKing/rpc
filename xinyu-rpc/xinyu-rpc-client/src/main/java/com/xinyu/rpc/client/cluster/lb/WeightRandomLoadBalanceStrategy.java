
package com.xinyu.rpc.client.cluster.lb;


import com.xinyu.rpc.annotation.XYrpcLoadBalance;
import com.xinyu.rpc.client.cluster.LoadBalanceStrategy;
import com.xinyu.rpc.provider.ServiceProvider;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

@XYrpcLoadBalance(strategy = "w_random")
public class WeightRandomLoadBalanceStrategy implements LoadBalanceStrategy {

    /**
     * 根据权重随机选择
     * @param serviceProviders
     * @return
     */
    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        List<ServiceProvider> newList=new ArrayList<>();
        for (ServiceProvider serviceProvider : serviceProviders) {
            int weight=serviceProvider.getWeight();
            for (int i = 0; i < weight; i++) {
                newList.add(serviceProvider);
            }
        }
        int index= RandomUtils.nextInt(0,newList.size());
        return newList.get(index);
    }
}
