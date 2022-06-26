package com.atguigu.gmall.product.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

//    int a = 1;
    //打印OOM；dump文件
//    Map<String,byte[]> map = new HashMap<>();

    @GetMapping("/hello")
    public String hello(){
        //TODO 业务  一旦发生OOM：jvm疯狂gc、应用假死（人活着，处理不了请求）可能会导致整个系统的雪崩；
        // 线程池？非常优秀的资源控制方式，防止资源无限量开辟而导致的OOM
//        map.put(UUID.randomUUID().toString(),new byte[1024*1024]);
        return "ok";
    }


    @Autowired
    StringRedisTemplate redisTemplate;


    @Value("${server.port}")
    String port;
    //lock
    @GetMapping("/incr")
    public String incr(){
        //1、取值
        System.out.println("="+port);
        int anInt = 0;
//        synchronized (this){ //分布式下没用
//
//        }
        //加锁.锁的粒度越细。才能提升吞吐量
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "1");
        while (!lock){ //阻塞式等锁  lock.lock();
            //没加到锁
            lock = redisTemplate.opsForValue().setIfAbsent("lock", "1");
        }

        //抢成功
        String num = redisTemplate.opsForValue().get("num");
        //2、加1
        anInt = Integer.parseInt(num);
        anInt = anInt +1;
        //3、改掉
        redisTemplate.opsForValue().set("num",anInt+"");
        //释放锁
        redisTemplate.delete("lock");  //lock.unlock()
        return ">>>"+anInt;

//        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "1");
//        if(lock){
//            String num = redisTemplate.opsForValue().get("num");
//            //2、加1
//            anInt = Integer.parseInt(num);
//            anInt = anInt +1;
//            //3、改掉
//            redisTemplate.opsForValue().set("num",anInt+"");
//            //释放锁
//            redisTemplate.delete("lock");
//        }else {
////           return incr(); //stack overflow
//
//        }


    }
}
