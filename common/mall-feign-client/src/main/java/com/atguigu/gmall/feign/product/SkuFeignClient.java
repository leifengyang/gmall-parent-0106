package com.atguigu.gmall.feign.product;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.vo.CategoryView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/rpc/inner/product")
@FeignClient("service-product")
//不同feignclient用了同一个 @FeignClient 的名字就得开启 bean定义信息重写功能

public interface SkuFeignClient {

    /**
     * 查询skuInfo信息
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId);


    /**
     * 根据c3Id查询完整路径
     */
    @GetMapping("/categoryview/{c3Id}")
    public Result<CategoryView> getCategoryView(@PathVariable("c3Id")Long c3Id);


    /**
     * 根据skuId和spuId查询出当前商品spu定义的所有销售属性名和值以及标记出当前sku是哪一对组合
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/sku/saleattr/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>>  getSaleAttr(@PathVariable("skuId") Long skuId,
                                                  @PathVariable("spuId") Long spuId);


    /**
     * 查出这个sku对应的spu到底有多少个sku组合，以及每个sku销售属性值组合封装成Map（"值1|值2|值N":skuId）
     */
    @GetMapping("/spu/skus/saleattrvalue/json/{spuId}")
    Result<String> getSpudeAllSkuSaleAttrAndValue(@PathVariable("spuId") Long spuId);


    @GetMapping("/sku/price/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable("skuId") Long skuId);

}
