package com.atguigu.gmall.item.controller;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("/lock")
@RestController
public class LockTestController {

    @Autowired
    RedissonClient redissonClient;


    @GetMapping("/b")
    public String b() throws InterruptedException {
        String lockKey = "lock-test";
        RLock lock = redissonClient.getLock(lockKey);

        lock.lock(30,TimeUnit.SECONDS); //1min以后自动释放
        System.out.println("b正在执行....");
        TimeUnit.MINUTES.sleep(3);
        lock.unlock();

        return "";

    }

    @GetMapping("/a")
    public String a() throws InterruptedException {
        String lockKey = "lock-test";
        //1、获取一把锁
        RLock lock = redissonClient.getLock(lockKey);//可重入锁
        /**
         * 特效：
         * 1、锁有自动过期时间：30s
         * 2、Redisson的所有操作都是原子的
         * 3、锁可以自动续期
         * 4、所有自己指定锁过期时间的都不会自动续期；【不要自己指定时间】。
         * 1）、加锁：占坑+过期时间。 lock：lua脚本原子
         * 2）、解锁：标志是谁加的锁，也不会解别人的锁
         * 3）、业务能续期；看门狗 30s
         */


        lock.lock();


//        boolean b = lock.tryLock(10,10,TimeUnit.SECONDS);
//        if(b){
//            System.out.println("加锁成功执行");
//
//            lock.unlock();
//        }

        return "errorlock";

//
//        //        redissonClient.getFairLock() //公平锁
//        //2、加锁
//        lock.lock();  //阻塞式等锁。默认设置了自动过期时间是30s
////        lock.lock(30,TimeUnit.DAYS); //指定过期时间
//
////        boolean b = lock.tryLock(); //试一下，不阻塞等待；这次没加上锁返回false；
//        //long waitTime, long leaseTime, TimeUnit unit
//        //试一下，有限等待waitTime时间，如果没加到返回false；如果加到返回true，锁自动过期时间为10s
////        boolean tryLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
//
//        try {
//            System.out.println("执行业务。。。。");
//            TimeUnit.SECONDS.sleep(20);
//            System.out.println("执行业务结束。。。。");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }

    }

}
