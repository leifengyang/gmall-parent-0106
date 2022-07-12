package com.atguigu.gmall.pay.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

public interface PayService {
    /**
     * 为指定的订单生成一个alipay页面
     * @param orderId
     * @return
     */
    String generatePayPage(Long orderId) throws AlipayApiException;

    /**
     * 验签
     * @param params
     * @return
     */
    boolean checkSign(Map<String, String> params) throws AlipayApiException;
}
