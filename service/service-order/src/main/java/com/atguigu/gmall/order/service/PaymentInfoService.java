package com.atguigu.gmall.order.service;


import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.payment.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author lfy
* @description 针对表【payment_info(支付信息表)】的数据库操作Service
* @createDate 2022-07-08 11:43:31
*/
public interface PaymentInfoService extends IService<PaymentInfo> {

    void savePayment(Map<String, String> map, OrderInfo orderInfo);
}
