package com.atguigu.gmall.cart.rcp;


import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rpc/inner/cart")
@RestController
public class CartController {


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

        //TODO 添加商品到购物车
        AddSuccessVo vo =  cartService.addToCart(skuId,num);



        return Result.ok(vo);
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