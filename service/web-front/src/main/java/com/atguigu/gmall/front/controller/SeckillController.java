package com.atguigu.gmall.front.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.seckill.SeckillFeignClient;
import com.atguigu.gmall.model.activity.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SeckillController {


    @Autowired
    SeckillFeignClient seckillFeignClient;


    /**
     * 秒杀商品列表页
     * @param model
     * @return
     */
    @GetMapping("/seckill.html")
    public String seckillPage(Model model){
        //每个参与秒杀的商品：{skuId、skuDefaultImg、skuName、costPrice、price、num、stockCount}
        Result<List<SeckillGoods>> result = seckillFeignClient.getCurrentDaySeckillGoods();
        model.addAttribute("list",result.getData());
        return "seckill/index";
    }


    /**
     * 获取秒杀商品详情
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/seckill/{skuId}.html")
    public String seckillGoodsDetail(@PathVariable("skuId")Long skuId,
                                     Model model){

        Result<SeckillGoods> goodsDetail = seckillFeignClient.getGoodsDetail(skuId);
        model.addAttribute("item",goodsDetail.getData());
        return "seckill/item";
    }

    //http://activity.gmall.com/seckill/queue.html?skuId=46&skuIdStr=0d9bc4a65dfd6ae3576fca22ee9f92d1
    @GetMapping("/seckill/queue.html")
    public String queue(@RequestParam("skuId") Long skuId,
                        @RequestParam("skuIdStr") String code,
                        Model model){

        model.addAttribute("skuId",skuId);
        model.addAttribute("skuIdStr",code);
        return "seckill/queue";
    }



}
