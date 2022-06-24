package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.dto.CategoryViewDo;
import com.atguigu.gmall.product.mapper.CategoryViewMapper;
import com.atguigu.gmall.product.service.CategoryViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author lfy
* @description 针对表【category_view】的数据库操作Service实现
* @createDate 2022-06-24 14:20:36
*/
@Service
public class CategoryViewServiceImpl extends ServiceImpl<CategoryViewMapper, CategoryViewDo>
    implements CategoryViewService{

    @Autowired
    CategoryViewMapper viewMapper;
    @Override
    public CategoryViewDo getViewByC3Id(Long c3Id) {
        QueryWrapper<CategoryViewDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("c3id",c3Id);
        CategoryViewDo viewDo = viewMapper.selectOne(queryWrapper);
        return viewDo;
    }
}




