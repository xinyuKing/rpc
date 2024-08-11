
package com.xinyu.rpc.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RpcClientConfiguration {

    @Value("${rpc.client.zk.root}")
    private String zkRoot;

    @Value("${rpc.client.zk.addr}")
    private String zkAddr;

    @Value("${server.port}")
    private String znsClientPort;

    @Value("${rpc.client.api.package}")
    private String rpcClientApiPackage;

    @Value("${rpc.cluster.strategy}")
    private String rpcClientClusterStrategy;

    @Value("${rpc.client.zk.timeout}")
    private Integer connectTimeout;
}
