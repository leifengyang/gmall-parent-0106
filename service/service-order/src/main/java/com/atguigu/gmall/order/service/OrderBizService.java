package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.ware.OrderSpiltVo;
import com.atguigu.gmall.model.vo.ware.OrderSplitRespVo;
import com.atguigu.gmall.model.vo.ware.WareFenBuVo;

import java.util.List;

public interface OrderBizService {


    OrderConfirmVo getOrderConfirmData();

    /**
     * 生成一个交易令牌
     * @return
     */
    String generateTradeToken();

    /**
     * 校验token
     * @param token  前端传来的令牌
     * @return
     */
    boolean checkTradeToken(String token);


    /**
     * 提交订单： 给数据库保存信息
     * @param tradeNo
     * @param order
     * @return
     */
    Long submitOrder(String tradeNo, OrderSubmitVo order);

    /**
     * 保存订单
     * @param order
     */
    OrderInfo saveOrder(String tradeNo,OrderSubmitVo order);

    /**
     * 关闭用户订单
     * @param orderId
     * @param userId
     */
    void closeOrder(Long orderId, Long userId);

    /**
     * 拆单
     * @param vo
     * @return
     */
    List<OrderSplitRespVo> splitOrder(OrderSpiltVo vo);


    /**
     * 保存子订单
     * @param orderInfo
     * @param buVo
     */
    OrderSplitRespVo saveChildOrder(OrderInfo orderInfo, WareFenBuVo buVo);
}
