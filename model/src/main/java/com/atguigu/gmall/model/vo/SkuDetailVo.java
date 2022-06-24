package com.atguigu.gmall.model.vo;


import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDetailVo {

    //sku对应的三级分类
    private CategoryView categoryView;
    //sku信息
    private SkuInfo skuInfo;
    //sku价格
    private BigDecimal price;
    //
    private List<SpuSaleAttr> spuSaleAttrList;

    private String valuesSkuJson;
}
