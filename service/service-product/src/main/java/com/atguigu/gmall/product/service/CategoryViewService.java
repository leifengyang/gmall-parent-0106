package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.dto.CategoryViewDo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lfy
* @description 针对表【category_view】的数据库操作Service
* @createDate 2022-06-24 14:20:36
*/
public interface CategoryViewService extends IService<CategoryViewDo> {

    /**
     * 根据三级分类id去 CategoryView 视图中查出精确的完整分类路径
     * @param c3Id
     * @return
     */
    CategoryViewDo getViewByC3Id(Long c3Id);
}
