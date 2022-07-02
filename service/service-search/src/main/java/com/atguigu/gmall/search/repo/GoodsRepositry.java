package com.atguigu.gmall.search.repo;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


//启动之前安装好ik分词器
@Repository
public interface GoodsRepositry extends PagingAndSortingRepository<Goods,Long> {



    //结构确定的检索DSL，就可以直接这么来写
//    @Query("{\n" +
//            "  \"query\": {\n" +
//            "    \"bool\": {\n" +
//            "      \"must\": [\n" +
//            "        {\"term\": {\n" +
//            "          \"category3Id\": {\n" +
//            "            \"value\": \"${c3Id}\"\n" +
//            "          }\n" +
//            "        }}\n" +
//            "      ]\n" +
//            "    }\n" +
//            "  }\n" +
//            "}\n")
//    public void search(Long c3Id);


}
