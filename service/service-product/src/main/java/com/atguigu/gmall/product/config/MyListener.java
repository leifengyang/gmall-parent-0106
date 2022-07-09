package com.atguigu.gmall.product.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyListener implements SpringApplicationRunListener {
    private SpringApplication springApplication;
    private String[] args;
    public MyListener(SpringApplication springApplication,String[] args){
        this.springApplication = springApplication;
        this.args = args;
    }

    @Override
    public void starting() {
        System.out.println("容器正在启动...");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("容器启动完成");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("容器正在运行中");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("运行时环境准备好...");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("IOC容器已加载...");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("IOC容器已准备好...");
    }

}
