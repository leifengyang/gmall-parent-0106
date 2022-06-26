package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

//@SpringBootTest(classes = ProductMainApplication.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void redistest01(){
//        redisTemplate.opsForValue();  //key-value(String)
//        redisTemplate.opsForSet(); //key-value(集合)
//        redisTemplate.opsForHash(); //key-value(Map[k,v])
//        redisTemplate.opsForList();//key-value(List)

        redisTemplate.opsForValue().set("hello","world");
        System.out.println("保存完成...");




    }

    @Test
    void queryTest(){
        String hello = redisTemplate.opsForValue().get("hello");
        System.out.println(hello);
    }
}
