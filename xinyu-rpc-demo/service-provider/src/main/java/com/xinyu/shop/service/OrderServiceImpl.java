package com.xinyu.shop.service;


import com.xinyu.rpc.annotation.XYrpcService;
import com.xinyu.rpc.server.config.RpcServerConfiguration;
import com.xinyu.shop.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;


@XYrpcService(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RpcServerConfiguration serverConfiguration;

    @Override
    public String getOrder(String userId, String orderNo) {
        return serverConfiguration.getServerPort() +"---"+serverConfiguration.getRpcPort()+"---Congratulations, The RPC call succeeded,orderNo is "+orderNo +",userId is " +userId;
    }
}
