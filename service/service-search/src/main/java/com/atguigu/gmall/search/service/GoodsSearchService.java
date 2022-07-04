package com.atguigu.gmall.search.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParam;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;

public interface GoodsSearchService {

    /**
     * ES上架商品
     * @param goods
     */
    void upGoods(Goods goods);

    /**
     * ES下架商品
     * @param skuId
     */
    void downGoods(Long skuId);


    /**
     * 商品检索
     * @param param
     * @return
     */
    SearchResponseVo search(SearchParam param);

    /**
     * 增加热度分
     * @param skuId
     * @param score
     */
    void incrHotScore(Long skuId, Long score);
}
