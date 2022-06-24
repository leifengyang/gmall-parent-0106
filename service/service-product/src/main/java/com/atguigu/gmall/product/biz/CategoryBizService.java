package com.atguigu.gmall.product.biz;

import com.atguigu.gmall.model.vo.CategoryVo;

import java.util.List;

/**
 * 和分类有关的复杂业务，都封装在biz包下
 */
public interface CategoryBizService {

    List<CategoryVo> getCategorys();
}
