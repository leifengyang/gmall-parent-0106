package com.atguigu.gmall.product;


import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * SpringBoot只会扫描自己主类所在的包和子包
 * 主：       com.atguigu.gmall.product
 * swagger:  com.atguigu.gmall.common.config
 */

@EnableTransactionManagement  //开启基于注解的自动事务管理
@Import(Swagger2Config.class)
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper") //批量扫描就无需在每个Mapper接口上标注@Mapper
@SpringCloudApplication
public class ProductMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class,args);
    }
}
