package com.atguigu.gmall.seckill.service;

import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.to.mq.SeckillQueueMsg;

public interface SeckillBizService {

    /**
     * 提前上架第二天参与秒杀的商品
     * @param formatDate
     */
    void uploadSeckillGoods(String formatDate);


    /**
     * 生成某个商品的秒杀码
     * @param skuId
     * @return
     */
    String generateSeckillCode(Long skuId);

    /**
     * 秒杀下单
     * @param skuId
     * @param skuIdStr
     */
    void seckillOrderSubmit(Long skuId, String skuIdStr);

    /**
     * 生成秒杀订单
     * @param msg
     */
    void generateSeckillOrder(SeckillQueueMsg msg);

    /**
     * 检查订单状态
     * @param skuId
     * @return
     */
    ResultCodeEnum checkOrderStatus(Long skuId);
}
