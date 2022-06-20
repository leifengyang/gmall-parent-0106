package com.atguigu.gmall.model.list;

import lombok.Data;

// 封装查询条件
@Data
public class SearchParam {

    // ?category3Id=61&trademark=2:华为&props=23:4G:运行内存&order=1:desc
    private Long category1Id;;//三级分类id
    private Long category2Id;
    private Long category3Id;
    // trademark=2:华为
    private String trademark;//品牌

    private String keyword;//检索的关键字

    // 排序规则
    // 1:hotScore 2:price
    private String order = ""; // 1：综合排序/热点  2：价格

    // props=23:4G:运行内存
    //平台属性Id 平台属性值名称 平台属性名
    private String[] props;//页面提交的数组

    private Integer pageNo = 1;//分页信息
    private Integer pageSize = 3; // 每页默认显示的条数


}
