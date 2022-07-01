package com.atguigu.gmall.search;


import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repo.PersonRepositry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class EsRepositryTest {

    @Autowired
    PersonRepositry personRepositry; //基本的crud


    //https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.operations.resttemplate
    @Autowired
    ElasticsearchRestTemplate esrestTemplate; //高级操作


    @Test
    public void testesrestTemplate(){
        //自定义检索操作用和这个
//        esrestTemplate.search(,)

        //自定义保存操作
//        esrestTemplate.index()

        //自定义删除操作
//        esrestTemplate.delete();

        //自定义修改操作
//        esrestTemplate.update();

        //bulk批量操作
//        esrestTemplate.bulkUpdate();
    }



    //1、查出年龄大于20的人

    //2、查询住在北京市并且年龄小于20 或者 id是5的人

    //更多操作：https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/
    @Test
    void testQuery2(){
        //中文分词，  模糊查"北京市"；"北"，"京","市"
        List<Person> all = personRepositry
                .findAllByAddressLikeAndAgeLessThanOrIdEquals("北京市", 20, 5L);
        all.stream().forEach(item-> System.out.println(item));



    }



    @Test
    void testQuery(){
        List<Person> all = personRepositry.findAllByAgeGreaterThan(20);
        all.stream().forEach(item-> System.out.println(item));
    }



    @Test
    void testSaveMulti() {

        List<Person> people = Arrays
                .asList(
                        new Person(1L, "张三1", "aaa1@qq.com", 18, "北京市昌平1区"),
                        new Person(2L, "张三2", "aaa2@qq.com", 19, "北京市昌平2区"),
                        new Person(3L, "张三3", "aaa3@qq.com", 20, "北京市昌平3区"),
                        new Person(4L, "张三4", "aaa4@qq.com", 21, "北京市昌平4区"),
                        new Person(5L, "张三5", "aaa5@qq.com", 22, "北京市昌平5区"),
                        new Person(6L, "张三6", "aaa5@qq.com", 10, "武汉市昌平5区"));

        personRepositry.saveAll(people);



    }


    /**
     * POST /person/_update/1
     * {
     * "doc": {
     * "username" : "张三2"
     * }
     * }
     * 自定义的复杂操作；自己发rest请求，自己拼json请求体
     */

    @Test
    void testUpdate() {
        Person person = new Person();
        person.setId(1L); //
        person.setUsername("张三");
        person.setAddress("111");
        person.setAge(19);

        //age不带，age字段是null，es就会自动剔除这个字段
        personRepositry.save(person);

        //rest

    }


    @Test
    void testPersonRepo() {
        Person person = new Person();
        person.setId(1L); //
        person.setUsername("张三");
        person.setAge(18);


        personRepositry.save(person);


        System.out.println("保存成功....");

        Optional<Person> byId = personRepositry.findById(1L);
        System.out.println("查询的结果：" + byId.get());
    }
}
