package com.atguigu.gmall.product;


import com.atguigu.gmall.common.annotation.EnableAutoHandleException;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * SpringBoot只会扫描自己主类所在的包和子包
 * 主：       com.atguigu.gmall.product
 * swagger:  com.atguigu.gmall.common.config
 *
 * 引入Redis：
 * 1、引入 spring-boot-starter-data-redis
 * 2、SpringBoot做了自动配置【RedisAutoConfiguration】；
 *      1）、redis所有可配置的属性在 RedisProperties 中
 *      2）、容器中以下组件能用
 *          RedisTemplate<Object, Object>： key-value： jdk的默认序列化；我们一般都推荐序列化为json字符串
 *          StringRedisTemplate = RedisTemplate<String, String>： key-value都是string，自己把对象转为你喜欢的字符串（json）然后给redis存
 *
 *
 *
 * 能不能用到 RedissonConfiguration ?
 * 启动只扫描自己包下的 com.atguigu.gmall.product
 *                   com.atguigu.gmall.common.config
 * 结论：抽取的配置类可以放到任何地方，如果项目加载不到的话，使用 @Import 精准导入指定配置
 *
 */




//@EnableCache
//@EnableRedisson  //导入Redisson的配置

@EnableAutoHandleException
@EnableScheduling //开启定时调度
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.search")
@EnableTransactionManagement  //开启基于注解的自动事务管理
@Import({Swagger2Config.class})
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper") //批量扫描就无需在每个Mapper接口上标注@Mapper
@SpringCloudApplication
public class ProductMainApplication {

    public static void main(String[] args) {
        //1、ApplicationRunner、CommandLineRunner  容器启动完成以后会调用这些观察者

        //2、SpringApplicationRunListener:  观察者模式完成的事件机制
        SpringApplication.run(ProductMainApplication.class,args);
    }
}
