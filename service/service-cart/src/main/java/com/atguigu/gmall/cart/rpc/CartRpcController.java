package com.atguigu.gmall.cart.rpc;


import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rpc/inner/cart")
@RestController
public class CartRpcController {


    @Autowired
    CartService cartService;
    /**
     * skuId 商品添加到购物车
     * @return
     */
    @GetMapping("/add/{skuId}")
    public Result<AddSuccessVo> addSkuToCart(@PathVariable("skuId") Long skuId,
                                             @RequestParam("num") Integer num){

        //获取当前用户信息. 隐式透传
//        UserAuth auth = AuthContextHolder.getUserAuth();

        AddSuccessVo vo =  cartService.addToCart(skuId,num);



        return Result.ok(vo);
    }


    /**
     * 获取购物车中选中的商品
     * @return
     */
    @GetMapping("/checked/items")
    public Result<List<CartInfo>> getCheckedCartItems(){
        String cartKey = cartService.determinCartKey();
        List<CartInfo> item = cartService.getAllCheckedItem(cartKey);
        return Result.ok(item);
    }

    /**
     * 删除选中的所有商品
     * @return
     */
    @GetMapping("/delete/checked")
    public Result deleteChecked(){

        cartService.deleteChecked();

        return Result.ok();
    }
}



//        Object newProxyInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(), this.getClass().getInterfaces(), new InvocationHandler() {
//
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                //1、自己
//                return null;
//            }
//        });

//        cartService.addToCart(skuId,num,userId);