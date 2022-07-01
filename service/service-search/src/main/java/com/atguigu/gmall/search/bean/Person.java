package com.atguigu.gmall.search.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Document(indexName = "person")  //Person这种类型的bean在es中自动对应一个索引库叫 person
public class Person {

    @Id  //标注主键
    private Long id;

    @Field(value = "username",type= FieldType.Text)
    private String username; //varchar  text

    @Field(value = "email")
    private String email;

    @Field(value = "age")
    private Integer age;

    @Field(value = "address")
    private String address;

}
