package com.atguigu.gmall.model.list;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

// Index = goods , Type = info  es 7.8.0 逐渐淡化type！  修改！
@Data
@Document(indexName = "goods" , shards = 3,replicas = 2)
public class Goods {
    // 商品Id skuId
    @Id
    private Long id;

    //Keyword 不要拆分，一个完整的关键字
    @Field(type = FieldType.Keyword, index = false)
    private String defaultImg; //自动决定，string text（全文检索，分词）；


    //  es 中能分词的字段，这个字段数据类型必须是 text！keyword 不分词！
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Double)
    private Double price;

    //  @Field(type = FieldType.Date)   6.8.1
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 新品

    @Field(type = FieldType.Long)
    private Long tmId;

    @Field(type = FieldType.Keyword)
    private String tmName;

    @Field(type = FieldType.Keyword)
    private String tmLogoUrl;

    @Field(type = FieldType.Long)
    private Long category1Id;

    @Field(type = FieldType.Keyword)
    private String category1Name;

    @Field(type = FieldType.Long)
    private Long category2Id;

    @Field(type = FieldType.Keyword)
    private String category2Name;

    @Field(type = FieldType.Long)
    private Long category3Id;

    @Field(type = FieldType.Keyword)
    private String category3Name;

    //  商品的热度！ 我们将商品被用户点查看的次数越多，则说明热度就越高！
    @Field(type = FieldType.Long)
    private Long hotScore = 0L;

    // 平台属性集合对象
    // Nested 支持嵌套查询
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrs;
    //如果集合类的属性未来要检索，必须声明这是一个Nested；嵌套的，才能正确被检索
    //每个的手机的平台属性都存起来,未来才能分析出，
    //这些检索到的100个商品到底涉及到了那些属性和那些值?


}
