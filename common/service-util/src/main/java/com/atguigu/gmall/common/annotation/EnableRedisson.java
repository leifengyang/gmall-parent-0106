package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.RedissonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(RedissonConfiguration.class)
public @interface EnableRedisson {


}
