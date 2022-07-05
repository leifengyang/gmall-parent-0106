package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;

public interface CartService {
    /**
     * 把商品添加到购物车
     * @param skuId
     * @param num
     * @return
     */
    AddSuccessVo addToCart(Long skuId, Integer num);

    /**
     * 返回当前购物车的redis中的key
     * 1、用户登录了： cart:info:用户id
     * 2、用户没登录： cart:info:临时id
     * @return
     */
    String determinCartKey();

    /**
     * 从指定的购物车中拿到指定商品信息
     * @param cartKey
     * @param skuId
     * @return
     */
    CartInfo getCartItem(String cartKey,Long skuId);


    /**
     * 把一个商品保存到购物车
     * @param item
     * @param cartKey
     */
    void saveItemToCart(CartInfo item,String cartKey);

    /**
     * 从远程的商品服务查到当前skuId对应的商品信息，并返回为CartInfo
     * @param skuId
     * @return
     */
    CartInfo getCartInfoFromRpc(Long skuId);


}
