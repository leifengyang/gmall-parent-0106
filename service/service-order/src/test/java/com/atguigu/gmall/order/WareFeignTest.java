package com.atguigu.gmall.order;


import com.atguigu.gmall.feign.ware.WareFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WareFeignTest {

    @Autowired
    WareFeignClient wareFeignClient;

    @Test
    void test01(){
        String stock = wareFeignClient.hasStock(43L, 10);
        System.out.println("库存："+stock);
    }
}
