package com.atguigu.gmall.seckill.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.seckill.mapper.SeckillGoodsMapper;
import com.atguigu.gmall.seckill.service.SeckillGoodsCacheService;
import com.atguigu.gmall.seckill.service.SeckillGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lfy
 * @description 针对表【seckill_goods】的数据库操作Service实现
 * @createDate 2022-07-13 09:24:31
 */
@Slf4j
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods>
        implements SeckillGoodsService {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    SeckillGoodsCacheService seckillGoodsCacheService;

    @Override
    public List<SeckillGoods> getCurrentDaySeckillGoods() {
        //1、查询今天参与秒杀的所有商品.
        String currentDay = DateUtil.formatDate(new Date());

        //2、查询指定日期的所有商品
        return getDaySeckillGoodsFromCache(currentDay);
    }

    @Override
    public List<SeckillGoods> getDaySeckillGoodsFromCache(String day) {

        //1、先从缓存中获取
        List<SeckillGoods> cached = seckillGoodsCacheService.getCachedSeckillGoods(day);

        return cached;
    }

    @Override
    public List<SeckillGoods> getDaySeckillGoodsFromDb(String day) {
        List<SeckillGoods> goods = seckillGoodsMapper.getDaySeckillGoods(day);
        return goods;
    }

    @Override
    public SeckillGoods getSeckillGood(Long skuId) {
        //
        SeckillGoods goods = seckillGoodsCacheService.getSeckillGood(skuId);
        return goods;
    }

    @Override
    public long deduceSeckillStockCount(Long skuId, int num) {
        //数据库非负扣库存 update seckill_goods set stock_count=stock_count-1 where sku_id=49
        long l = 0;
        try {
            l = seckillGoodsMapper.updateSeckillStockCount(skuId,num);
        }catch (Exception e){
            log.info("skuId【{}】秒杀库存不足：",skuId);
        }
        return l;
    }
}




