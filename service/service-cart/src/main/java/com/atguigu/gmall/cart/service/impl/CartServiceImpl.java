package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import com.atguigu.gmall.model.vo.user.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class CartServiceImpl implements CartService {



    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SkuFeignClient skuFeignClient;

    /**
     * 把商品添加到购物车
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public AddSuccessVo addToCart(Long skuId, Integer num) {
        AddSuccessVo successVo = new AddSuccessVo();

        //1、决定使用哪个购物车
        String cartKey = determinCartKey();
        //2、添加；原来购物车有没有这个商品，如果没有就是新增，有就是数量叠加
        //3、尝试从购物车中获取到这个商品
        CartInfo item = getCartItem(cartKey, skuId);
        if(item == null){
            //3.1 没有就新增
            CartInfo info = getCartInfoFromRpc(skuId);
            //3.2 设置数量
            info.setSkuNum(num);
            //3.3 同步到redis
            saveItemToCart(info,cartKey);


            successVo.setSkuDefaultImg(info.getImgUrl());
            successVo.setSkuName(info.getSkuName());
            successVo.setId(info.getSkuId());
        }else {
            //3.2 有就修改数量
            item.setSkuNum(item.getSkuNum() + num);
            //3.3 同步到redis
            saveItemToCart(item,cartKey);

            successVo.setSkuDefaultImg(item.getImgUrl());
            successVo.setSkuName(item.getSkuName());
            successVo.setId(item.getSkuId());
        }


        return successVo;
    }

    @Override
    public String determinCartKey() {
        //1、拿到用户信息
        UserAuth userAuth = AuthContextHolder.getUserAuth();
        String cartKey = "";
        if(userAuth.getUserId()!=null){
            //用户登录了
            cartKey = RedisConst.CART_INFO_PREFIX + userAuth.getUserId();
        }else {
            //如果没有临时id。前端自己会造一个传给我们
            cartKey = RedisConst.CART_INFO_PREFIX + userAuth.getTempId();
        }
        return cartKey;
    }

    @Override
    public CartInfo getCartItem(String cartKey, Long skuId) {
        //1、拿到一个能操作hash的对象
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        //2、获取cartKey购物车指定的skuId商品
        String json = ops.get(cartKey, skuId.toString());
        //3、逆转
        if(StringUtils.isEmpty(json)){
            return null;
        }
        CartInfo info = JSONs.toObj(json, CartInfo.class);
        return info;
    }

    @Override
    public void saveItemToCart(CartInfo item, String cartKey) {
        //1、拿到一个能操作hash的对象
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Long skuId = item.getSkuId();
        //2、给redis保存一个hash数据
        ops.put(cartKey,skuId.toString(),JSONs.toStr(item));
    }

    @Override
    public CartInfo getCartInfoFromRpc(Long skuId) {

        Result<CartInfo> info = skuFeignClient.getCartInfoBySkuId(skuId);
        CartInfo data = info.getData();
        return data;
    }


}
