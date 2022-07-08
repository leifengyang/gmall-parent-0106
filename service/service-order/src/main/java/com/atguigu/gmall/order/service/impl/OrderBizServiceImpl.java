package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.order.CartOrderDetailVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderBizServiceImpl implements OrderBizService {


    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    SkuFeignClient skuFeignClient;

    @Autowired
    WareFeignClient wareFeignClient;

    @Override
    public OrderConfirmVo getOrderConfirmData() {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        //1、获取用户收货地址列表
        confirmVo.setUserAddressList(userFeignClient.getUserAddress().getData());

        //CartOrderDetailVo
        List<CartInfo> checkedItems = cartFeignClient.getCheckedCartItems().getData();

        List<CartOrderDetailVo> detailVos = checkedItems.stream()
                .map(cartInfo -> {
                    CartOrderDetailVo detailVo = new CartOrderDetailVo();
                    //查询最新价格
                    Result<BigDecimal> price = skuFeignClient.get1010SkuPrice(cartInfo.getSkuId());
                    detailVo.setOrderPrice(price.getData());
                    detailVo.setImgUrl(cartInfo.getImgUrl());
                    detailVo.setSkuName(cartInfo.getSkuName());
                    detailVo.setSkuNum(cartInfo.getSkuNum());
                    //远程调用库存系统;
                    String stock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
                    detailVo.setStock(stock);
                    return detailVo;
                }).collect(Collectors.toList());

        //2、获取购物车中选中的需要结算的商品
        confirmVo.setDetailArrayList(detailVos);


        //3、总数量
        Integer totalNum = checkedItems.stream()
                .map(cartInfo -> cartInfo.getSkuNum())
                .reduce((o1, o2) -> o1 + o2)
                .get();

        confirmVo.setTotalNum(totalNum);

        //4、总金额  每个商品实时价格*数量 的加和
        BigDecimal totalAmount = detailVos.stream()
                .map(cart -> cart.getOrderPrice().multiply(new BigDecimal(cart.getSkuNum())))
                .reduce((o1, o2) -> o1.add(o2))
                .get();

        confirmVo.setTotalAmount(totalAmount);

        //5、防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        confirmVo.setTradeNo(token);


        return confirmVo;
    }
}
