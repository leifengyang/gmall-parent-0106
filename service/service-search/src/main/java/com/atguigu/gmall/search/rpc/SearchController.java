package com.atguigu.gmall.search.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParam;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import com.atguigu.gmall.search.service.GoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rpc/inner/search")
@RestController
public class SearchController {

    @Autowired
    GoodsSearchService goodsSearchService;


    /**
     * 商品检索
     * @return
     */
    @PostMapping("/goods")
    public Result<SearchResponseVo> search(@RequestBody SearchParam param){

        //检索
        SearchResponseVo vo = goodsSearchService.search(param);

        return Result.ok(vo);
    }



    /**
     * 商品上架
     * @param goods
     * @return
     */
    @PostMapping("/up")
    public Result upGoods(@RequestBody Goods goods){

        goodsSearchService.upGoods(goods);
       return Result.ok();
    }

    /**
     * 商品下架
     * @param skuId
     * @return
     */
    @GetMapping("/down/{skuId}")
    public Result downGoods(@PathVariable("skuId") Long skuId){
        goodsSearchService.downGoods(skuId);
        return Result.ok();
    }



}
