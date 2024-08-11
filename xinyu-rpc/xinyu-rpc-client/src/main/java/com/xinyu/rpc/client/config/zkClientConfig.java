
package com.xinyu.rpc.client.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class zkClientConfig {

    private static final int EXPIRE_SECONDS = 86400;

    @Autowired
    private RpcClientConfiguration configuration;

    @Bean
    public ZkClient zkClient() {
        return new ZkClient(configuration.getZkAddr(), configuration.getConnectTimeout());

    }
}
