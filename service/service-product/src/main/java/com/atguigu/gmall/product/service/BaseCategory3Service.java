package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lfy
* @description 针对表【base_category3(三级分类表)】的数据库操作Service
* @createDate 2022-06-20 14:51:04
*/
public interface BaseCategory3Service extends IService<BaseCategory3> {

    /**
     * 获取某个二级分类下的所有三级分类
     * @param category2Id
     * @return
     */
    List<BaseCategory3> getCategory3By2Id(Long category2Id);
}
