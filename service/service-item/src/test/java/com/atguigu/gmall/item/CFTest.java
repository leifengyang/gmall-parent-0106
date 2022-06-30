package com.atguigu.gmall.item;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CFTest {

    public static void main2(String[] args) throws ExecutionException, InterruptedException {
        //1、查询价格
        CompletableFuture<BigDecimal> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("正在查询价格：" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int i = 199 / 0;
                    return new BigDecimal("" + i);
                })
                .exceptionally(t -> {
                    return new BigDecimal("9999");
                });


        //3、编排-感知异步的完成状态（异步的try-catch）


        //4、编排-错误兜底。（当异步发生错误以后）
//        future.exceptionally(throwable -> {
//            System.out.println("异常收到了：" + throwable);
//            System.out.println("正在查缓存");
//            return new BigDecimal("99999");
//        });

        System.out.println("最终价格：" + future.get()); //9999

        Thread.currentThread().join();

    }

    public static void main(String[] args) throws Exception {

        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("start:" + Thread.currentThread().getName());
        //1、返回基本信息， 小米8

        CompletableFuture<String> baseInfofuture = CompletableFuture.supplyAsync(() -> {
            //全是其他线程在运行
            System.out.println("查询出基本信息：" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("查询出基本信息完成:" + Thread.currentThread().getName());
            return "小米8";
        });

        // then: 接下来干什么
        // when: 当某个事件发生以后回调我们的逻辑
        // async: 异步方式
        // 参数： 传线程池，用自己的线程池，否则用默认线程池

//        CompletableFuture<U> thenApply(Function<? super T,? extends U> fn);
//        CompletableFuture<Void> thenRun(Runnable); 不能接参数，不能返回参数
//        CompletableFuture<Void> thenAccept(Consumer); 能接参数，但不能返回参数



        //2、基本信息查完以后，要查价格
        // R apply(T t);
        CompletableFuture<BigDecimal> future = baseInfofuture.thenApply(val -> {
            System.out.println(val + "：商品的id=" + 1);
            System.out.println("正在查询1号商品价格");
            return new BigDecimal("9999");
        });

        System.out.println("价格："+future.get());


        //2、查询价格
//        CompletableFuture<BigDecimal> decimalFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("正在查询价格：" + Thread.currentThread().getName());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int i = 199 / 0;
//            return new BigDecimal("" + i);
//        });


        //3、查询图片
//        CompletableFuture<String> imgFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("正在查询图片：" + Thread.currentThread().getName());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "美女.jpg";
//        });

        //4、查询销售属性
//        CompletableFuture<String> attrFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("正在查询销售属性：" + Thread.currentThread().getName());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "8+128";
//        });

        //5、查询 兄弟其他组合
//        CompletableFuture<String> otherFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("正在查询兄弟们组合：" + Thread.currentThread().getName());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "还有8+256和9+128";
//        });


        System.out.println("end:" + Thread.currentThread().getName());


        //1、编排-所有都完成
//        CompletableFuture.allOf(baseInfofuture,
//                decimalFuture,
//                imgFuture, attrFuture, otherFuture).get(); //这些Future全部完成以后
        //2、编排-需要感知异步异常、完成状态

//        System.out.println("商品信息：名字：【"+baseInfofuture.get()+"】;" +
//                "价格：【"+decimalFuture.get()+"】；图片：【"+imgFuture.get()+"】；" +
//                "销售属性：【"+attrFuture.get()+"】；" +
//                "兄弟组合：【"+otherFuture.get()+"】");

        watch.stop();
        System.out.println("耗时：" + watch.getTotalTimeMillis());


        Thread.currentThread().join();

    }
}
