package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.vo.CategoryView;
import com.atguigu.gmall.model.vo.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    SkuFeignClient skuFeignClient;


    @Override
    public SkuDetailVo getItemDetail(Long skuId) {
        SkuDetailVo vo = new SkuDetailVo();
        //2、sku的info
        Result<SkuInfo> skuInfo = skuFeignClient.getSkuInfo(skuId);
        SkuInfo info = skuInfo.getData();
        vo.setSkuInfo(info);


        //1、sku所在的分类。
        Long category3Id = info.getCategory3Id();
        //按照三级分类id查出所在的完整分类信息
        Result<CategoryView> categoryView = skuFeignClient.getCategoryView(category3Id);
        vo.setCategoryView(categoryView.getData());


        //3、sku的价格
        vo.setPrice(info.getPrice());

        //4、sku的销售属性列表
        Long spuId = info.getSpuId();
        Result<List<SpuSaleAttr>> saleAttr = skuFeignClient.getSaleAttr(skuId, spuId);
        if(saleAttr.isOk()){
            vo.setSpuSaleAttrList(saleAttr.getData());
        }



        //5、得到一个sku对应的spu的所有sku的组合关系
        Result<String> value = skuFeignClient.getSpudeAllSkuSaleAttrAndValue(spuId);
        vo.setValuesSkuJson(value.getData());

        return vo;
    }
}
