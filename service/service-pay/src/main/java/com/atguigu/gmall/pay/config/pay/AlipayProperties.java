package com.atguigu.gmall.pay.config.pay;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "app.alipay")
public class AlipayProperties {

    private String appId;
    private String merchantPrivateKey;  //商户私钥
    private String alipayPublicKey; //支付宝公钥
    private String notifyUrl; //异步通知地址
    private String returnUrl;//同步跳转地址（支付成功以后，浏览器跳转到的页面）
    private String signType; //签名类型
    private String charset;
    private String gatewayUrl; //支付宝网关


}
