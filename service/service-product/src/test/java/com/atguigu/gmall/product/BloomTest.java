package com.atguigu.gmall.product;


import com.atguigu.gmall.common.constant.RedisConst;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BloomTest {

    @Autowired
    RedissonClient redissonClient;

    @Test
    void test01(){
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);

        boolean contains = filter.contains(99L);
        System.out.println(contains);
    }
}
