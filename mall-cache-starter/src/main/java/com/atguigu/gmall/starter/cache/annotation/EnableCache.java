package com.atguigu.gmall.starter.cache.annotation;


import com.atguigu.gmall.starter.cache.MallCacheAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(MallCacheAutoConfiguration.class)  //导入缓存的自动配置
@EnableAspectJAutoProxy   //开启基于注解的apsectj切面功能
public @interface EnableCache {

}
