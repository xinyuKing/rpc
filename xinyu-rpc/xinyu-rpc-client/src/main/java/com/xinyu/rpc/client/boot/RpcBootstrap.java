
package com.xinyu.rpc.client.boot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RpcBootstrap {


    @Autowired
    private RpcClientRunner clientRunner;


    @PostConstruct
    public void initRpcClient() {
        clientRunner.run();
    }

}
