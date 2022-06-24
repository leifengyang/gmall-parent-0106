package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lfy
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
* @createDate 2022-06-21 09:01:28
* @Entity com.atguigu.gmall.product.domain.SpuSaleAttr
*/
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

    /**
     * 查询spu对应的所有销售属性名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(@Param("spuId") Long spuId);

    /**
     * 根据skuId和spuId查询出当前商品spu定义的所有销售属性名和值以及标记出当前sku是哪一对组合
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrAndMarkSkuSaleValue(@Param("skuId") Long skuId,
                                                        @Param("spuId") Long spuId);
}




