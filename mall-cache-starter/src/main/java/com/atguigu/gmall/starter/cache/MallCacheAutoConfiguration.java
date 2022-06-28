package com.atguigu.gmall.starter.cache;


import com.atguigu.gmall.starter.cache.apsect.CacheAspect;
import com.atguigu.gmall.starter.cache.component.CacheService;
import com.atguigu.gmall.starter.cache.component.impl.CacheServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 商城缓存的自动配置类
 *
 */
@Import(RedissonConfiguration.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class MallCacheAutoConfiguration {


    @Bean  //缓存切面
    public CacheAspect cacheAspect(){
        return new CacheAspect();
    }

    @Bean  //操作缓存的组件
    public CacheService cacheService(){
        return new CacheServiceImpl();
    }

}
