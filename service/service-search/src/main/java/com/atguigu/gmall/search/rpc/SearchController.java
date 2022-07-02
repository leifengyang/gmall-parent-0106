package com.atguigu.gmall.search.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParam;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import com.atguigu.gmall.search.service.GoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/rpc/inner/search")
@RestController
public class SearchController {

    @Autowired
    GoodsSearchService goodsSearchService;


    /**
     * 商品检索
     * 参数：
     *    普通参数：  k=v&k=v&1000个
     *              SearchParam param2；从请求参数中找到所有和javaBean属性一样的封装进去
     *    文件上传项：
     *              header=二进制流;@RequestPart("header") MultipartFile header
     *              shz=二进制流;@RequestPart("shz") MultipartFile shz
     *              sfzs=二进制流;二进制流;二进制流  @RequestPart("sfzs") MultipartFile[] sfzs
     *              MultipartFile file
     *    自定义参数： {k:v,k:v,k:v}   @RequestBody SearchParam param； 自己把json转成SearchParam
     * @return
     */
    @PostMapping ("/goods")
    public Result<SearchResponseVo> search(@RequestBody SearchParam param, HttpServletRequest request){

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
