package com.atguigu.gmall.product.config.bloom;


import com.atguigu.gmall.product.service.BloomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆的配置类
 */
@Slf4j
@Configuration
public class BloomConfiguration {


    @Autowired
    BloomService bloomService;

    //第一次项目启动的时候初始化布隆
    //SpringBoot应用一启动以后，会从容器中拿到 ApplicationRunner，执行他们的run方法

    @Bean
    public ApplicationRunner applicationRunner(){
       return new ApplicationRunner(){

            @Override
            public void run(ApplicationArguments args) throws Exception {
                log.info("应用启动完成正在初始化布隆...");
                bloomService.initBloom();
            }
        };
    }

}
