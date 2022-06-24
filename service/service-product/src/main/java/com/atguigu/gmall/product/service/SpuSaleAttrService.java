package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lfy
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-06-21 09:01:28
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    /**
     * 获取spu的所有销售属性名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    /**
     * 根据skuId和spuId查询出当前商品spu定义的所有销售属性名和值以及标记出当前sku是哪一对组合
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrAndMarkSkuSaleValue(Long skuId, Long spuId);
}
