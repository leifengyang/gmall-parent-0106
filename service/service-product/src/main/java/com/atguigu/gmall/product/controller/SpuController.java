package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/product")
@RestController
public class SpuController {


    @Autowired
    SpuInfoService spuInfoService;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    /**
     * 分页查询某个分类下的spu
     * @param category3Id
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuByCategoryId(@RequestParam("category3Id") Long category3Id,
                                     @PathVariable("page")Long page,
                                     @PathVariable("limit")Long limit){

        Page<SpuInfo> p = new Page<>(page,limit);
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);

        //分页查询
        Page<SpuInfo> result = spuInfoService.page(p, wrapper);

        return Result.ok(result);
    }


    /**
     * 商品spu_info保存
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpu(@RequestBody SpuInfo spuInfo){
        //spuinfo的大保存
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }


    /**
     * 查询spuid对应的所有图片
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId")Long spuId){
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(wrapper);

        return Result.ok(list);
    }


    /**
     * 获取spu的所有销售属性名和值
     * @param spuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId")Long spuId){

        //获取spu的所有销售属性名和值
        List<SpuSaleAttr> saleAttrs = spuSaleAttrService.getSpuSaleAttrList(spuId);

        return Result.ok(saleAttrs);
    }
}
