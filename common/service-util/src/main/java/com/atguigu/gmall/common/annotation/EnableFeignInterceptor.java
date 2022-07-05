package com.atguigu.gmall.common.annotation;


import com.atguigu.gmall.common.interceptor.RequestHeaderSetFeignIntercetpor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Import(RequestHeaderSetFeignIntercetpor.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableFeignInterceptor {
}
