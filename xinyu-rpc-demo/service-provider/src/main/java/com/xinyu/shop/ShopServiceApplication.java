package com.xinyu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.xinyu.shop","com.xinyu.rpc"})
@ComponentScan({"com.xinyu.shop","com.xinyu.rpc"})
public class ShopServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServiceApplication.class,args);
    }

}
