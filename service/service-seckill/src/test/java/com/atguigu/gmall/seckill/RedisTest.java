package com.atguigu.gmall.seckill;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void test01(){
        for (int i = 0; i < 10; i++) {
            Long aLong = redisTemplate.opsForValue().increment("seckill:code:133f6a4264921af29ae0dd888c6e4288");
            System.out.println(aLong);
        }

    }
}
