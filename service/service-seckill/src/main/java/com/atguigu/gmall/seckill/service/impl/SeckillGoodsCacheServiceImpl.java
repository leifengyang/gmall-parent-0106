package com.atguigu.gmall.seckill.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.seckill.service.SeckillGoodsCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SeckillGoodsCacheServiceImpl implements SeckillGoodsCacheService {

    @Autowired
    StringRedisTemplate redisTemplate;

    //当天参与秒杀的所有商品都会在这个Map中
    Map<Long, SeckillGoods> localCache = new ConcurrentHashMap<>();


    /**
     * 多级缓存操作
     *
     * @param day
     * @param goods
     */
    @Override
    public void saveToCache(String day, List<SeckillGoods> goods) {
        String key = RedisConst.SECKILL_GOODS_CACHE_PREFIX + "" + day;

        //1、给redis存一份
        goods.stream().forEach(item -> {
            //双缓存写入
            //保存到缓存中
            redisTemplate.opsForHash().put(key, item.getSkuId().toString(), JSONs.toStr(item));
            //2、给本地存一份
            localCache.put(item.getSkuId(), item);
        });
        //1、最好设置一个过期时间，不过，每天秒杀结束以后，定时任务盘点当天账单数据。此时也会删除秒杀缓存
        redisTemplate.expire(key, 2, TimeUnit.DAYS);

    }

    @Override
    public List<SeckillGoods> getCachedSeckillGoods(String day) {
        //1、先查询本地缓存中的数据

        List<SeckillGoods> goods = localCache.values().stream()
                .sorted((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()))
                .collect(Collectors.toList());
        log.info("本地秒杀缓存命中数据：{}", goods.size());
        if (goods == null || goods.size() == 0) {
            //本地缓存没有，有可能上次发生了宕机
            //2、查询redis中上架的当天的商品数据即可
            HashOperations<String, String, String> ops = redisTemplate.opsForHash();
            List<String> json = ops.values(RedisConst.SECKILL_GOODS_CACHE_PREFIX + day);
            goods = json.stream()
                    .map(str -> JSONs.toObj(str, SeckillGoods.class))
                    .sorted((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()))
                    .collect(Collectors.toList());
            log.info("分布式秒杀缓存命中数据");
            //3、放回本地
            goods.stream().forEach(item -> localCache.put(item.getSkuId(), item));
        }
        return goods;
    }

    @Override
    public SeckillGoods getSeckillGood(Long skuId) {
        SeckillGoods goods = localCache.get(skuId);
        if (goods == null) {
            //1、看下是否是宕机导致的？ 宕机导致的就同步下远程缓存。
            if(localCache.size() == 0){
                //本地缓存没数据，就可能是宕机了。本地此时就同步上了远程数据
                getCachedSeckillGoods(DateUtil.formatDate(new Date()));
                goods = localCache.get(skuId);
                return goods;
            }
        }
        return goods;
    }
}
