package com.atguigu.gmall.search;


import com.atguigu.gmall.model.vo.search.SearchParam;
import com.atguigu.gmall.search.service.GoodsSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchTest {

    @Autowired
    GoodsSearchService searchService;




    @Test
    void searchTest(){
        SearchParam param = new SearchParam();
        param.setCategory3Id(61L);
//        param.setKeyword("手机");

        // props=3:6GB:运行内存&props=4:64GB:机身存储
//        param.setProps( new String[]{"3:6GB:运行内存","4:64GB:机身存储"});


        searchService.search(param);

    }
}
