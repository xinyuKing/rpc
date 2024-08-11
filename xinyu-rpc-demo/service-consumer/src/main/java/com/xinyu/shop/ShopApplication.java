package com.xinyu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.xinyu.shop","com.xinyu.rpc"})
@ComponentScan({"com.xinyu.shop","com.xinyu.rpc"})
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class,args);
    }
}

