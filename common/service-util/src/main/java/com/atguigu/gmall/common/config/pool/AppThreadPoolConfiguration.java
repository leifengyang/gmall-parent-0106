package com.atguigu.gmall.common.config.pool;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的配置类
 */
@EnableConfigurationProperties(ThreadPoolProperties.class)
@Configuration
public class AppThreadPoolConfiguration {


    /**
     * 多个线程池：
     * 1、核心业务线程池（订单）
     * 2、非核心业务线程池（短信、邮件、xx）；
     * @return
     */
    @Bean
    public  ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties properties){
        //1、核心业务线程池；
        ThreadFactory threadFactory = new ThreadFactory() {
            int i = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                //自己的线程名
                thread.setName("service-item-thread-" + (i++));
//                thread.setPriority(10);
                return thread;
            }
        };
        //队列的长度就是系统的峰值*2~10；
        /**
         * int corePoolSize,     核心大小：4； 初始大小
         * int maximumPoolSize,  最大大小：8； 最多8个线程
         * long keepAliveTime,   存活时间：60  多开出来的线程的弹性伸缩时间
         * TimeUnit unit,        时间单位：s、
         * BlockingQueue<Runnable> workQueue, 阻塞队列。 调整能等待的任务的数量
         * ThreadFactory threadFactory,   帮我们new Thread()的工厂
         * RejectedExecutionHandler handler  拒绝策略
         */
       return new ThreadPoolExecutor(properties.getCorePoolSize(),
               properties.getMaximumPoolSize(),
               properties.getKeepAliveTime(), TimeUnit.SECONDS,
               new LinkedBlockingQueue<>(properties.getWorkQueueSize()),
               threadFactory,
               new ThreadPoolExecutor.CallerRunsPolicy()); //500m 1kb java堆内存的1/10~1/5；


    }




}
