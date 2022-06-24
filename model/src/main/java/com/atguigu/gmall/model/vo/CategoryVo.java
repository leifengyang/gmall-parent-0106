package com.atguigu.gmall.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 三级分类vo
 */
@Data
public class CategoryVo {
    private Long categoryId;    //1
    private String categoryName; //手机
    private List<CategoryVo> categoryChild;
}
