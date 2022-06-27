package com.atguigu.gmall.product.biz.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.vo.CategoryVo;
import com.atguigu.gmall.product.biz.CategoryBizService;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CategoryServiceImpl implements CategoryBizService {
    @Autowired
    BaseCategory1Mapper category1Mapper;


    @Autowired
    StringRedisTemplate redisTemplate;

    ReentrantLock lock = new ReentrantLock();  //集群模式锁不住



    @Override
    public List<CategoryVo> getCategorys() {
        return  category1Mapper.getCategorys();
    }




    //配合缓存
    public List<CategoryVo> getCategorys1111() {
        //1、先看缓存
        String categorys = redisTemplate.opsForValue().get("categorys");
        //2、缓存中没有
        if(StringUtils.isEmpty(categorys)){  //"null"
            System.out.println("缓存不命中，准备查库");
            Boolean absent = redisTemplate.opsForValue().setIfAbsent("lock", "11");//Redis Documentation: SETNX
            if(absent){
                //抢到分布式锁了，抢到查库
                List<CategoryVo> vos =  category1Mapper.getCategorys();
            }else {
                //没抢到
            }
//3、查询数据库. 以前学的juc-lock、synchronized，只能在单点使用，分布式集群情况下，各自机器锁各自的。
//            lock.lock();
//            synchronized (this){ //加锁，百万并发是否同一把锁。
//            }
//            lock.unlock();

            //this是谁？当前service对象。这个对象只有一个。说明大家都是抢同一把锁。
            List<CategoryVo> vos =  category1Mapper.getCategorys();

            //4、放到缓存: 无论数据库有没有查到这个数据，都放缓存。
            if(vos == null){
                //数据库中没有。缓存的时间短一点
                redisTemplate.opsForValue().set("categorys", JSONs.toStr(null),30, TimeUnit.MINUTES);
            }else {
                //数据库中有。缓存的时间长一点
                redisTemplate.opsForValue().set("categorys", JSONs.toStr(null),7, TimeUnit.DAYS);
            }

            return vos;
        }

        System.out.println("缓存命中，直接返回:"+categorys);
        //5、缓存中有,直接返回
        List<CategoryVo> data = JSONs.toObj(categorys, new TypeReference<List<CategoryVo>>() {
        });
        System.out.println(categorys+"：反序列化后："+data);
        return data;
    }


}
