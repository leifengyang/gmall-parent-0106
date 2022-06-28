package com.atguigu.gmall.starter.cache.annotation;


import java.lang.annotation.*;

/**
 * 缓存注解；
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cache {

    //key="categorys"
    String key() default "";

    //指定布隆过滤器的名字。  默认不用
    String bloomName() default "";


    //布隆判定时用的值
    String bloomIf() default "";

    long ttl() default 1000*60*30L; //默认不说，数据就缓存30min
}
