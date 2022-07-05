package com.atguigu.gmall.cart;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void testredis(){
        redisTemplate.opsForValue().set("aaaa","aaa1");

        String aaaa = redisTemplate.opsForValue().get("aaaa");
        System.out.println(aaaa);
    }
}
