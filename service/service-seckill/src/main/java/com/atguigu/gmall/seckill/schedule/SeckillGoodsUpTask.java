package com.atguigu.gmall.seckill.schedule;


import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.seckill.service.SeckillBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Slf4j
@EnableScheduling  //开启定时任务
@Service
public class SeckillGoodsUpTask {


    @Autowired
    SeckillBizService seckillBizService;

    //每天最迟22:00结束当天秒杀。
    //每天晚上22:30 启动，上架第二天需要参与秒杀的所有商品
    @Scheduled(cron = "0 30 22 * * ?")
//    @Scheduled(cron = "0 * * * * ?")
    public void seckillGoodsUp(){
        log.info("定时任务正在上架第二天参与秒杀的所有商品");

        //开发环境
        String formatDate = DateUtil.formatDate(new Date());

        //生产环境
//        String nextDay = getNextDay();

        //把指定这天的所有商品保存到缓存中
        seckillBizService.uploadSeckillGoods(formatDate);
    }


    public String getNextDay(){
        LocalDate now = LocalDate.now();
        LocalDate plus = now.plus(1L, ChronoUnit.DAYS);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = plus.format(formatter);
        return format;

    }
}
