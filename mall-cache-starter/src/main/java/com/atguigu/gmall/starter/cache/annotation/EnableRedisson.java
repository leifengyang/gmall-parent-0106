package com.atguigu.gmall.starter.cache.annotation;

import com.atguigu.gmall.starter.cache.RedissonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(RedissonConfiguration.class)
public @interface EnableRedisson {


}
