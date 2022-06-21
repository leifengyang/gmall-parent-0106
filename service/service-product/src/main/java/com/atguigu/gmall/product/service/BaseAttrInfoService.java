package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lfy
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-06-21 09:01:27
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    /**
     * 查询分类下的所有属性名和值
     * @param c1Id
     * @param c2Id
     * @param c3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoWithValue(Long c1Id, Long c2Id, Long c3Id);

    /**
     * 保存平台属性名和值
     * @param baseAttrInfo
     */
    void saveAttrAndValue(BaseAttrInfo baseAttrInfo);

    /**
     * 修改平台属性名和值
     * @param baseAttrInfo
     */
    void updateAttrAndValue(BaseAttrInfo baseAttrInfo);
}
