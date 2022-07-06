package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;

import java.util.List;

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


    /**
     * 获取当前用户购物车中所有商品
     * @return
     */
    List<CartInfo> getCartAllItem();

    /**
     * 获取指定购物车中所有商品
     * @return
     */
    List<CartInfo> getCartAllItem(String cartKey);

    /**
     * 修改购物车中某个商品状态
     * @param skuId
     * @param status
     */
    void updateCartItemStatus(Long skuId, Integer status);

    /**
     * 删除选中的商品
     */
    void deleteChecked();

    /**
     * 从指定购物车中获取所有被选中的商品
     * @param cartKey
     * @return
     */
    List<CartInfo> getAllCheckedItem(String cartKey);

    /**
     * 删除购物车中指定的商品
     * @param skuId
     */
    void deleteCartItem(Long skuId);

    /**
     * 设置临时购物车的过期时间
     */
    void setTempCartExpire();
}
