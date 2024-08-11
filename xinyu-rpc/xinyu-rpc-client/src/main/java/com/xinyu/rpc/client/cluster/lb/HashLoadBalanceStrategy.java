
package com.xinyu.rpc.client.cluster.lb;



import com.xinyu.rpc.annotation.XYrpcLoadBalance;
import com.xinyu.rpc.client.cluster.LoadBalanceStrategy;
import com.xinyu.rpc.provider.ServiceProvider;
import com.xinyu.rpc.util.IpUtil;

import java.util.List;

@XYrpcLoadBalance(strategy = "hash")
public class HashLoadBalanceStrategy implements LoadBalanceStrategy {
    /**
     * 基于ip hashcode选择服务提供者
     * @param serviceProviders
     * @return
     */

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        // 基于ip hashcode
        String realIp = IpUtil.getRealIp();
        int hashCode = realIp.hashCode();
        int index = Math.abs(hashCode % serviceProviders.size());
        return serviceProviders.get(index);
    }
}
