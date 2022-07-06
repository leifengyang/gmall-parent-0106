package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import com.atguigu.gmall.model.vo.user.UserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
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

        //设置过期时间；
        setTempCartExpire();

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

        //3、判断购物车是否已经满了
        if(ops.size(cartKey) < RedisConst.CART_SIZE_LIMIT){
            ops.put(cartKey,skuId.toString(),JSONs.toStr(item));
        }else {
            throw new GmallException(ResultCodeEnum.OUT_OF_CART);
        }

    }

    @Override
    public CartInfo getCartInfoFromRpc(Long skuId) {

        Result<CartInfo> info = skuFeignClient.getCartInfoBySkuId(skuId);
        CartInfo data = info.getData();
        return data;
    }

    @Override
    public List<CartInfo> getCartAllItem() {
        //0、是否需要合并： 只要tempId对应的购物车有东西，并且还有userId；合并操作
        UserAuth auth = AuthContextHolder.getUserAuth();
        if(auth.getUserId()!=null && !StringUtils.isEmpty(auth.getTempId())){
            //有可能合并购物车
            //1、如果临时购物车有东西，就合并；只需要判断临时购物车是否存在
            Boolean hasKey = redisTemplate.hasKey(RedisConst.CART_INFO_PREFIX + auth.getTempId());
            if(hasKey){
                //2、redis有临时购物车。就需要先合并，再获取购物车中所有数据
                //3、拿到临时购物车的商品
                List<CartInfo> cartInfos = getCartAllItem(RedisConst.CART_INFO_PREFIX + auth.getTempId());

                cartInfos.forEach(tempItem->{
                    //4、每个临时购物车中的商品都添到用户购物车;
                    addToCart(tempItem.getSkuId(),tempItem.getSkuNum());
                });
                //5、删除临时购物车
                redisTemplate.delete(RedisConst.CART_INFO_PREFIX + auth.getTempId());
            }
        }

        String cartKey = determinCartKey();
        List<CartInfo> allItem = getCartAllItem(cartKey);
        RequestAttributes oldRequest = RequestContextHolder.getRequestAttributes(); //1线程
        //每一个查一下价格
        CompletableFuture.runAsync(()->{
            log.info("提交一个实时改价异步任务");
            allItem.forEach(item->{
                //2线程，共享给另一个线程
                RequestContextHolder.setRequestAttributes(oldRequest);
                //一旦异步，因为在异步线程中 RequestContextHolder.getRequestAttributes() 是获取不到老请求，
                // 1、feign拦截器就拿不到老请求  2、feign拦截器啥都不做（tempId，userId）都无法继续透传下去
                Result<BigDecimal> price = skuFeignClient.getSkuPrice(item.getSkuId());
                RequestContextHolder.resetRequestAttributes(); //ThreadLocal的所有东西一定要有放，有删
                if(!item.getSkuPrice().equals(price.getData())){
                    log.info("正在后台实时更新 【{}】 购物车，【{}】商品的价格；原【{}】，现：【{}】",
                            cartKey,item.getSkuId(),item.getSkuPrice(),price.getData());
                    //发现价格发生了变化
                    item.setSkuPrice(price.getData());
                    //同步到redis
                    saveItemToCart(item,cartKey);
                }
            });
        });

        return allItem;

    }

    @Override
    public List<CartInfo> getCartAllItem(String cartKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();

        List<CartInfo> collect = ops.values(cartKey)
                .stream()
                .map(jsonStr -> JSONs.toObj(jsonStr, CartInfo.class))
                .sorted((pre,next)-> (int)(next.getCreateTime().getTime() - pre.getCreateTime().getTime()))
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public void updateCartItemStatus(Long skuId, Integer status) {
        String cartKey = determinCartKey();
        CartInfo cartItem = getCartItem(cartKey, skuId);
        cartItem.setIsChecked(status);

        //同步redis
        saveItemToCart(cartItem,cartKey);



    }

    @Override
    public void deleteChecked() {

        String cartKey = determinCartKey();

        //1、找到购物车总所有被选中的商品，并删除他们
        List<CartInfo> cartInfos = getAllCheckedItem(cartKey);


        Object[] ids = cartInfos.stream()
                .map(info -> info.getSkuId().toString())
                .toArray();


        //2、删除他们
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        ops.delete(cartKey,ids);

    }

    @Override
    public List<CartInfo> getAllCheckedItem(String cartKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();

        List<CartInfo> infos = ops.values(cartKey)
                .stream()
                .map(jsonStr -> JSONs.toObj(jsonStr, CartInfo.class))
                .filter(info -> info.getIsChecked() == 1)
                .collect(Collectors.toList());

        return infos;
    }

    @Override
    public void deleteCartItem(Long skuId) {
        String cartKey = determinCartKey();
        //Long(序列化) == String(序列化)
        redisTemplate.opsForHash().delete(cartKey, skuId.toString());

    }

    @Override
    public void setTempCartExpire() {
        UserAuth auth = AuthContextHolder.getUserAuth();

        //用户只操作临时购物车
        if (!StringUtils.isEmpty(auth.getTempId()) && auth.getUserId() == null) {
            //用户带了临时token；
            //1、如果有临时购物车就设置过期时间。
            Boolean hasKey = redisTemplate.hasKey(RedisConst.CART_INFO_PREFIX + auth.getTempId());
            if(hasKey){
                //临时购物车有一年的时间。
                redisTemplate.expire(RedisConst.CART_INFO_PREFIX + auth.getTempId(),365, TimeUnit.DAYS);
            }
        }
    }


}
