package com.atguigu.gmall.seckill.service;


import com.atguigu.gmall.model.activity.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lfy
* @description 针对表【seckill_goods】的数据库操作Service
* @createDate 2022-07-13 09:24:31
*/
public interface SeckillGoodsService extends IService<SeckillGoods> {

    /**
     * 获取当天参与秒杀的所有商品
     * @return
     */
    List<SeckillGoods> getCurrentDaySeckillGoods();

    /**
     * 查询指定某天参与秒杀的所有商品
     * @param day
     * @return
     */
    List<SeckillGoods> getDaySeckillGoodsFromCache(String day);

    /**
     * 查询指定某天参与秒杀的所有商品
     * @param day
     * @return
     */
    List<SeckillGoods> getDaySeckillGoodsFromDb(String day);

    /**
     * 获取某个秒杀商品
     * @param skuId
     * @return
     */
    SeckillGoods getSeckillGood(Long skuId);

    /**
     * 扣除指定商品的秒杀库存
     * @param skuId
     * @param num
     * @return
     */
    long deduceSeckillStockCount(Long skuId, int num);
}
