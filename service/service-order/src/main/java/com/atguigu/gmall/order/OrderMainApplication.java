package com.atguigu.gmall.order;


import com.atguigu.gmall.common.annotation.EnableAutoHandleException;
import com.atguigu.gmall.common.annotation.EnableFeignInterceptor;
import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.AppMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;


@Import(AppMybatisPlusConfig.class)
@EnableAutoHandleException
@EnableFeignInterceptor
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign.user",
        "com.atguigu.gmall.feign.cart",
        "com.atguigu.gmall.feign.ware",
        "com.atguigu.gmall.feign.product"
})
@EnableRabbit  //未来系统中流转的都是json
@MapperScan(basePackages = "com.atguigu.gmall.order.mapper")
@EnableThreadPool
@SpringCloudApplication
public class OrderMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}
