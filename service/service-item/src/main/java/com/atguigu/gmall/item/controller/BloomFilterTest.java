package com.atguigu.gmall.item.controller;


import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BloomFilterTest {

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/bloom/init")
    public String initBloom(){
        RBloomFilter<Object> filter = redissonClient.getBloomFilter("hello-bloom");

        //1、初始化。只需要允许一次
        //long expectedInsertions, double falseProbability误判率
        filter.tryInit(1000000,0.000001);

        //2、放数据
        filter.add(40L);
        filter.add(41L);
        filter.add(42L);
        filter.add(43L);
        filter.add(44L);
        filter.add(45L);

        return "ok";
    }
    @GetMapping("/bloom/test/{num}")
    public String testBloom(@PathVariable("num") Long num){
        RBloomFilter<Object> filter = redissonClient.getBloomFilter("hello-bloom");
        boolean contains = filter.contains(num);
        return contains+"";
    }
}
