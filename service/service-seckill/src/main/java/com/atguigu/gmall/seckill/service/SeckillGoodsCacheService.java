package com.atguigu.gmall.seckill.service;


import com.atguigu.gmall.model.activity.SeckillGoods;

import java.util.List;

/**
 * 秒杀商品缓存服务
 */
public interface SeckillGoodsCacheService {
    /**
     * 把秒杀商品保存到缓存
     * @param day
     * @param goods
     */
    void saveToCache(String day, List<SeckillGoods> goods);

    /**
     * 查询缓存中当天参与秒杀的商品数据
     * @param day
     * @return
     */
    List<SeckillGoods> getCachedSeckillGoods(String day);

    /**
     * 获取秒杀商品详情。可以防止宕机。如果宕机后，启动第一次调用，会自动同步远程缓存
     * @param skuId
     * @return
     */
    SeckillGoods getSeckillGood(Long skuId);
}
