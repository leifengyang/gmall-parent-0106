package com.atguigu.gmall.model.vo;

import lombok.Data;


/**
 * 封装一个sku对应的精准三级分类的id和名字
 */
@Data
public class CategoryView {
    private Long category1Id;
    private String category1Name;

    private Long category2Id;
    private String category2Name;

    private Long category3Id;
    private String category3Name;
}
