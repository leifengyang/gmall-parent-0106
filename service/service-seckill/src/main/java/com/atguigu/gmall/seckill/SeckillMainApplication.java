package com.atguigu.gmall.seckill;


import com.atguigu.gmall.common.annotation.EnableAutoHandleException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;



@EnableRabbit
@EnableAutoHandleException
@MapperScan(basePackages = "com.atguigu.gmall.seckill.mapper")
@SpringCloudApplication
public class SeckillMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillMainApplication.class,args);
    }
}
