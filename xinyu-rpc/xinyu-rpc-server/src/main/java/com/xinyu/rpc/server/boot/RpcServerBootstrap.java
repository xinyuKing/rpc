package com.xinyu.rpc.server.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RpcServerBootstrap {


    @Autowired
    private RpcServerRunner rpcServerRunner;

    @PostConstruct
    public void initRpcServer() {
        rpcServerRunner.run();
    }


}
