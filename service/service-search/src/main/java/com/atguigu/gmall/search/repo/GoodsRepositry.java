package com.atguigu.gmall.search.repo;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


//启动之前安装好ik分词器
@Repository
public interface GoodsRepositry extends PagingAndSortingRepository<Goods,Long> {

}
