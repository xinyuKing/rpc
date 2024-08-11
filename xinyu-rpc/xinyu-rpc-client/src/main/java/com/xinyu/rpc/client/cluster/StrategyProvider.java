
package com.xinyu.rpc.client.cluster;


public interface StrategyProvider {

    LoadBalanceStrategy getStrategy();
}
