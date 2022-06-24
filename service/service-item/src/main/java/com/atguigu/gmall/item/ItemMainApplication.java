package com.atguigu.gmall.item;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


//调谁扫谁。
//每一个微服务不用扫自己的controller暴露的feignclient，微服务暴露的feignclient给别人用的，不是给自己用的。
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.product"}) //调谁导谁
@SpringCloudApplication
public class ItemMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}

