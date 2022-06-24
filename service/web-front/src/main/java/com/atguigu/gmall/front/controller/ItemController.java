package com.atguigu.gmall.front.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.item.ItemFeignClient;
import com.atguigu.gmall.model.vo.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品详情控制器
 */
@Controller
public class ItemController {


    @Autowired
    ItemFeignClient itemFeignClient;

    /**
     * 查询sku的详情
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId")Long skuId, Model model){
        //商品详情服务 - 查询商品的详情。
        //1、sku的基本信息【名字、售价...】
        //2、sku所在的完整分类
        //3、sku所属的spu的名字
        //4、sku的所有图片
        //5、sku的销售属性
        //6、大数据推荐商品（大数据算出喜好，这个商品的周边，保存到一张表中）
        //7、sku的平台属性
        //8、库存
        //9、【评价不用立即查。点击以后发送请求，获取评价数据】
        //10、商品介绍
        Result<SkuDetailVo> skuDetail = itemFeignClient.getSkuDetail(skuId);
        SkuDetailVo data = skuDetail.getData();

        //分类
        model.addAttribute("categoryView",data.getCategoryView());

        //sku信息
        model.addAttribute("skuInfo",data.getSkuInfo());

        //sku价格
        model.addAttribute("price",data.getPrice());

        //spu定义的所有销售属性名和值
        model.addAttribute("spuSaleAttrList",data.getSpuSaleAttrList());

        //valuesSkuJson： {“118|120”：49， “119|120”：50}
        //查出当前sku对应的spu到底有多少sku，
        // 并每个sku的销售属性值组合，按照 值组合为key，skuId为value，存到一个map中。再把这个map转为json给前端
        model.addAttribute("valuesSkuJson",data.getValuesSkuJson());

        //还要sku的平台属性。

        return "item/index";
    }
}
