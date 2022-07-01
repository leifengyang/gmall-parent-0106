package com.atguigu.gmall.model.vo.search;

import lombok.Data;

/**
 * 检索条件
 *      * 可能的传参
 *      * 分类：        1、category1Id=1&category2Id=2&category3Id=3
 *      * 关键字：      2、keyword=手机
 *      * 二次检索条件：
 *      *    品牌：    3、trademark=1:小米
 *      *    平台属性： 4、props=24:256G:机身内存&props=23:8G:运行内存&props=106:安卓手机:手机系统
 *      * 排序：
 *      *      5、order=1:desc
 *      * 页码：
 *      *      6、pageNo=1
 */
@Data
public class SearchParam {
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private String keyword;
    private String trademark;
    private String order;
    private String[] props;
    private Long pageNo;


}
