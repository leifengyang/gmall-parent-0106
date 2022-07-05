package com.atguigu.gmall.cart;


import com.atguigu.gmall.common.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignInterceptor
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
@SpringCloudApplication
public class CartMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartMainApplication.class,args);
    }
}
