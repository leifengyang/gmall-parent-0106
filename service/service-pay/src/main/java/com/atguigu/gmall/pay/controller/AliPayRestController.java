package com.atguigu.gmall.pay.controller;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/payment/alipay")
public class AliPayRestController {


    @Autowired
    PayService alipayService;

    @GetMapping(value = "/submit/{orderId}",produces = "text/html")
    public String getPayPage(@PathVariable("orderId") Long orderId) throws AlipayApiException {

        String html = alipayService.generatePayPage(orderId);
        //要给前端展示一个支付宝二维码收银台
        return html;

    }



}
