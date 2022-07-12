package com.atguigu.gmall.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.pay.config.pay.AlipayProperties;
import com.atguigu.gmall.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    AlipayProperties alipayProperties;

    @Autowired
    OrderFeignClient orderFeignClient;

    @Override
    public String generatePayPage(Long orderId) throws AlipayApiException {
        //1、拿到ali支付客户端

        //2、创建一个支付请求
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayProperties.getNotifyUrl());  //异步通知页地址
        request.setReturnUrl(alipayProperties.getReturnUrl());  //同步跳转页地址


        OrderInfo orderInfo = orderFeignClient.getOrderInfoByIdAndUserId(orderId).getData();
        //3、准备请求体数据; json能带这些：https://opendocs.alipay.com/open/028r8t?scene=22#%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0_2
        Map<String,String> body = new HashMap<>();
        //订单流水
        body.put("out_trade_no",orderInfo.getOutTradeNo());
        //订单价格
        body.put("total_amount",orderInfo.getTotalAmount().toString());
        //订单名称
        body.put("subject",orderInfo.getTradeBody().split("<br/>")[0]);
        body.put("product_code","FAST_INSTANT_TRADE_PAY");
        //自动收单（收单以后的订单就不能支付了） time_expire绝对时间 和 timeout_express相对时间
        String date = DateUtil.formatDate(orderInfo.getExpireTime(), "yyyy-MM-dd HH:mm:ss");
        body.put("time_expire",date);

        //业务参数
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        //TODO 生产环境用这种
//        body.put("business_params",""+userId);




        //4、设置支付请求体
        request.setBizContent(JSONs.toStr(body));

        //5、执行请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);


        if (response.isSuccess()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public boolean checkSign(Map<String, String> params) throws AlipayApiException {

        boolean checkV1 = AlipaySignature.rsaCheckV1(params,
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getCharset(),
                alipayProperties.getSignType());

        return checkV1;
    }
}
