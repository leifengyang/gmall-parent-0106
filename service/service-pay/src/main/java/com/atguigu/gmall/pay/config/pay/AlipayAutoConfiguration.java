package com.atguigu.gmall.pay.config.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayAutoConfiguration {

    @Bean
    public AlipayClient alipayClient(AlipayProperties properties){
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getAppId(),
                properties.getMerchantPrivateKey(),
                "json",
                properties.getCharset(),
                properties.getAlipayPublicKey(), properties.getSignType());
    }

}
