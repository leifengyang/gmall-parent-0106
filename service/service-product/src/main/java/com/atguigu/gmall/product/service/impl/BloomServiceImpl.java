package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.product.service.BloomService;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BloomServiceImpl implements BloomService {

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void initBloom() {
        //1、获取布隆过滤器
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);
        //2、是否已经存在
        if (filter.isExists()) {
            log.info("{} 布隆过滤器已经存在，跳过初始化",RedisConst.SKU_BLOOM_FILTER_NAME);
            return;
        }

        //3、初始化布隆
        //3.0、 初始化
        filter.tryInit(1000000,0.000001);
        //3.1、 查出所有商品的id
        List<Long> skuIds = skuInfoService.getSkuIds();
        //3.2、 加入布隆
        for (Long skuId : skuIds) {
            filter.add(skuId);
        }
        log.info("{} 布隆过滤器初始化完成：总计：{}",RedisConst.SKU_BLOOM_FILTER_NAME,skuIds.size());

    }

    @Override
    public void rebuildSkuBloom() {
        log.info("正在重建 {}布隆...",RedisConst.SKU_BLOOM_FILTER_NAME);
        //1、获取布隆过滤器
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);
        //2、删除
        filter.delete();

        //3、调初始化
        initBloom();
    }
}
