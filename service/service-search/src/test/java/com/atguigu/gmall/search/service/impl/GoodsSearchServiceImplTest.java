package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.search.service.GoodsSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class GoodsSearchServiceImplTest {


    @Autowired
    GoodsSearchService goodsSearchService;

    @Test
    void incrHotScore() {
        goodsSearchService.incrHotScore(49L,11L);
    }
}