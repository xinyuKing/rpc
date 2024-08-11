package com.xinyu.shop.controller.order;


import com.xinyu.rpc.annotation.XYrpcRemote;
import com.xinyu.shop.order.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @XYrpcRemote
    private OrderService orderService;

    @GetMapping("/getOrder")
    public String getOrder(String userId, String orderNo) {
        return orderService.getOrder(userId, orderNo);// 底层走网络调用
    }
}
