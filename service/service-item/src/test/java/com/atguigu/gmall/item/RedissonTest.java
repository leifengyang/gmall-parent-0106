package com.atguigu.gmall.item;


import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {


    @Autowired
    RedissonClient redissonClient;

    @Test
    void  redissontest(){
        System.out.println(redissonClient);
    }
}
