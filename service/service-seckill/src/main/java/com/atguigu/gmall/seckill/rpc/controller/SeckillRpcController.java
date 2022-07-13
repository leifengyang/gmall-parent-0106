package com.atguigu.gmall.seckill.rpc.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rpc/inner/seckill")
@RestController
public class SeckillRpcController {


    @Autowired
    SeckillGoodsService seckillGoodsService;
    /**
     * 获取当天参与秒杀的所有商品
     * @return
     */
    @GetMapping("/goods/currentDay")
    public Result<List<SeckillGoods>> getCurrentDaySeckillGoods(){

        List<SeckillGoods> seckillGoods = seckillGoodsService.getCurrentDaySeckillGoods();
        return Result.ok(seckillGoods);
    }

    /**
     * 获取某个秒杀商品详情
     * @param skuId
     * @return
     */
    @GetMapping("/goods/detail/{skuId}")
    public Result<SeckillGoods> getGoodsDetail(@PathVariable("skuId")Long skuId){

        SeckillGoods goods = seckillGoodsService.getSeckillGood(skuId);
        return Result.ok(goods);
    }
}
