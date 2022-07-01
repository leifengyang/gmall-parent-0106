package com.atguigu.gmall.feign.search;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/rpc/inner/search")
@FeignClient("service-search")
public interface SearchFeignClient {


    /**
     * 商品检索
     * SearchResponseVo == json == Map
     *
     * @return
     */
    @PostMapping("/goods")
    Result<Map<String,Object>> search(@RequestBody SearchParam param);

//    Result<SearchResponseVo> search(@RequestBody SearchParam param);




    @PostMapping("/up")
    Result upGoods(@RequestBody Goods goods);


    /**
     * 商品下架
     * @param skuId
     * @return
     */
    @GetMapping("/down/{skuId}")
    Result downGoods(@PathVariable("skuId") Long skuId);
}
