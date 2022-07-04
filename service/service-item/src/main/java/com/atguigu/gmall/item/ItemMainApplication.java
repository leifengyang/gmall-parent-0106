package com.atguigu.gmall.item;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


//调谁扫谁。
//每一个微服务不用扫自己的controller暴露的feignclient，微服务暴露的feignclient给别人用的，不是给自己用的。

/**
 * AOP
 * 1、切面； 所有通知方法都放在了一个组件（类），各个位置由这个类的对象切入执行
 * 2、通知方法：（Filter、拦截器、切面）
 *      目标方法执行之前要拦截到指定位置，来执行通知方法
 *      前置通知   @Before
 *      返回通知   @AfterReturning
 *      异常通知   @AfterThrowing
 *      后置通知   @After
 *      //以上是普通通知，只能感知到目标方法执行到这个位置，然后来执行我。 并不能拦截目标方法的执行
 *
 *      环绕通知   @Around
 *
 *
 *
 *   当前：com.atguigu.gmall.item
 *   切面：com.atguigu.gmall.starter.cache
 *
 */


@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.product","com.atguigu.gmall.feign.search"}) //调谁导谁
@SpringCloudApplication
public class ItemMainApplication {




    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}

