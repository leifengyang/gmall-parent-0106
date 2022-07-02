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

    private String trademark; //1:小米
    private String[] props; //props=3:6GB:运行内存&props=4:64GB:机身存储
    //3:6GB:运行内存   4:64GB:机身存储


    //以上都是查询条件

    private String order; //排序 order=2:asc  order=1:desc
    //2:asc,1:desc ,xxx
    //1：代表按照热度排序
    //2：代表按照价格排序
    //3：代表按照新品排序
    //xxxxxxx

    private Long pageNo = 1L; //分页，只取部分


}
