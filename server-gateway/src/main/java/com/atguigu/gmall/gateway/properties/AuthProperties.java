package com.atguigu.gmall.gateway.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    private List<String> loginUrl;//需要登录才能访问的

    private List<String> noAuthUrl;//任何情况都不能让浏览器访问的

    private String loginPage; //登录页地址
}
