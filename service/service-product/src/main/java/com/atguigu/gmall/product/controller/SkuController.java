package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.starter.cache.component.CacheService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * sku控制器
 */
@RequestMapping("/admin/product")
@RestController
public class SkuController {


    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    CacheService cacheService;

    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){

        skuInfoService.saveSkuInfo(skuInfo);

        return Result.ok();
    }

    /**
     * 分页查询sku信息
     * @param limit
     * @param page
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable("limit")Long limit,
                       @PathVariable("page")Long page){
        Page<SkuInfo> p = new Page<>(page,limit);
        Page<SkuInfo> result = skuInfoService.page(p);
        return Result.ok(result);
    }


    /**
     * 上架
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.upSku(skuId);
        return Result.ok();
    }

    /**
     * 下架
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId){
        skuInfoService.downSku(skuId);
        return Result.ok();
    }

    /**
     * 改价格
     */
    @GetMapping("/change/price/{skuId}")
    public Result changePrice(@PathVariable("skuId") Long skuId,
                              @RequestParam("price") BigDecimal price){

//        skuInfoService.changePrice(skuId,price);
        //延迟双删缓存
        cacheService.delayDoubleDelete(RedisConst.SKU_PRICE_CACHE_PREFIX+skuId);

        return Result.ok();
    }
}
