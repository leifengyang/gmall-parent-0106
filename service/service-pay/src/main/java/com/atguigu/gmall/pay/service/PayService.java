package com.atguigu.gmall.pay.service;

import com.alipay.api.AlipayApiException;

public interface PayService {
    /**
     * 为指定的订单生成一个alipay页面
     * @param orderId
     * @return
     */
    String generatePayPage(Long orderId) throws AlipayApiException;
}
