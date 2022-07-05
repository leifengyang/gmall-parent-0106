package com.atguigu.gmall.front;


import com.atguigu.gmall.common.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableDiscoveryClient
//@EnableCircuitBreaker


/**
 * 渲染前端页面的
 * 1、引入 thymeleaf-starter
 * 2、thymeleaf 的自动配置
 * 3、复制页面进去。
 *
 * 直接访问这个项目就能看到页面？
 * 1、只要templates下有个index.html，默认访问就展示这个页面；（首页Controller是默认有的）
 * 2、访问其他任何页面都需要自己写Controller进行跳转。
 *
 *
 * 远程调用openfeign（声明式远程调用）
 * 1、导入openfeign
 * 2、使用;
 *    1）、创建一个feign接口
 *    2）、指定远程调用信息
 * 3、使用 @EnableFeignClients
 *    SpringBoot 项目启动就会扫描主类所在的包以及子包下面的 所有标注了 @FeignClient 的类。
 *    会创建这些接口的代理对象。并放到容器中。
 *    以后想要远程调用，只需要@Autowired这个FeignClient接口，
 *    代理对象就会跟 @FeignClient 指定的远程服务建立连接发送请求，并得到远程数据。
 *    并且能根据方法的返回值类型，把远程的数据转为你指定的类型（自动反序列化）
 *
 * feignclient抽取以后：
 *   feign客户端所在的包： com.atguigu.gmall.feign
 *   当前微服务主程序包：   com.atguigu.gmall.front
 */
//说明接下来所有的feignclient在哪个包
@EnableFeignInterceptor
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign")  //开启所有声明式feignclient的远程能力
@SpringCloudApplication
public class FrontMainApplication {


    public static void main(String[] args) {
        SpringApplication.run(FrontMainApplication.class,args);
    }

}
