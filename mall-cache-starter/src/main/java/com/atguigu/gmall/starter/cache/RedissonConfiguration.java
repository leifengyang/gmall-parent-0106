package com.atguigu.gmall.starter.cache;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(RedisAutoConfiguration.class)  //redisson必须在Redis配置好以后再进行配置
@Configuration
public class RedissonConfiguration {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String redisHost){
        //1、代表redisson的配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();


        //Redis url should start with redis:// or rediss:// (for SSL connection)
        config.useSingleServer() //使用单节点服务器模式
              .setAddress("redis://"+host+":"+port)
              .setPassword(password); //制定redis地址
//        config.setLockWatchdogTimeout()//自定义看门狗时间
        //2、创建客户端
        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }
}
