package com.atguigu.gmall.search.repo;


import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  //明确告诉Spring这是操作一个数据仓库的  分页用 PagingAndSortingRepository
public interface PersonRepositry extends PagingAndSortingRepository<Person,Long> {

    //1、查出年龄大于20的人
    List<Person> findAllByAgeGreaterThan(Integer age);

    //2、查询住在北京市并且年龄小于20 或者 id是3的人  %xx%
    // "北京市昌平1区" ， "北京市"
    // 倒排索引： "北京市昌平1区"   北：1,2  京：1,2,3   模糊搜索"东京" "东" "京"   1,2,3
    List<Person> findAllByAddressLikeAndAgeLessThanOrIdEquals(String address, Integer age, Long id);


    boolean existsByIdEquals(Long id);

    Long countByAddressLike(String address);

    //只能简单查询

    //发送自己的dsl（领域结构语言）进行查询；



}
