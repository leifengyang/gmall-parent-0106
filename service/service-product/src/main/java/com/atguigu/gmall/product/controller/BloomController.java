package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.BloomService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/product")
@RestController
public class BloomController {

    @Autowired
    BloomService bloomService;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/sku/bloom/rebuild")
    public Result rebuildBloom(){
        bloomService.rebuildSkuBloom();
        return Result.ok();
    }

    @GetMapping("/sku/bloom/contains/{skuId}")
    public Result bloomTest(@PathVariable("skuId") Long skuId){
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);
        return Result.ok(filter.contains(skuId));
    }
}
