package com.atguigu.gmall.model.list;

import lombok.Data;

import java.io.Serializable;

// 品牌数据
@Data
public class SearchResponseTmVo implements Serializable {
    //当前属性值的所有值
    private Long tmId;
    //属性名称
    private String tmName;//网络制式，分类
    //图片名称
    private String tmLogoUrl;//网络制式，分类
}

