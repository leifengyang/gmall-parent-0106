package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class StreamAPI {





    @Test
    void completableFutureTest(){
        //声明式：@Transaction、.parallel()
        //编程式：

        //声明式的[异步编排工具]；CompletableFuture

        //1、怎么启动一个异步任务；
        //2、怎么得到异步的结果
        //3、怎么编排？

        //forkjoin线程池
    }

    @Test
    void functionTest(){
        Predicate<Integer> predicate = new Predicate<Integer>(){
            @Override
            public boolean test(Integer o) {
                return o % 2 ==0;
            }
        };

        System.out.println(predicate.test(12));
        System.out.println(predicate.test(7));


        //lambda表达式简写的函数式接口实现
        Predicate<Integer> predicate1 = (Integer o)-> {
            return o % 2 ==0;
        };

        Predicate<Integer> predicate2 = (o)-> {
            return o % 2 ==0;
        };

        Predicate<Integer> predicate3 = o -> {
            return o % 2 ==0;
        };

        Predicate<Integer> predicate4 = o -> o % 2 ==0;

    }

    @Test
    void test01(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        //R apply(T t); //串行流
        Integer integer = list.stream()
                .parallel()
                .filter(val -> {
                    System.out.println(val+"元素：正在被"+Thread.currentThread().getId()+"处理");
                    return val % 2 == 0;
                })
                .map(val -> val * 2)
                .filter(val -> val <= 10)
                .reduce((v1, v2) -> v1 + v2)
                .get();

        System.out.println(integer);

        //R apply(T t, U u)

    }

    @Test
    void  test(){
        //1、一堆数据
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        //2、找到这堆数据中的偶数，给他*2；

        //3、把>10的偶数不要

        //4、把剩下的偶数求个和

        Integer sum = 0;
        for (Integer integer : list) {
            if(integer % 2 == 0){
                integer = integer *2;
                if(integer <10){
                    sum+=integer;
                }
            }
        }

        System.out.println("sum:"+sum);
    }
}
