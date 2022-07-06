package com.atguigu.gmall.front.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {


    @Autowired
    CartFeignClient cartFeignClient;

//    public static Map<Thread,HttpServletRequest> threadLocal = new HashMap<>();
//    ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

    /**
     * 添加商品到购物车
     * ?skuId=49&skuNum=1&sourceType=query
     * @return
     */
    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model){


        //隐式传递userId
//        String userId = request.getHeader("UserId");
//        String userTempId = request.getHeader("UserTempId");
        //有放
//        threadLocal.put(Thread.currentThread(),request);
//        threadLocal.set(request);

        //
        Result<AddSuccessVo> result = cartFeignClient.addSkuToCart(skuId, skuNum);

        model.addAttribute("skuInfo",result.getData());
        model.addAttribute("skuNum",skuNum);
        //有删
//        threadLocal.remove(Thread.currentThread());
//        threadLocal.remove();
        return "cart/addCart";
    }



    @GetMapping("/cart.html")
    public String cartList(){

        return "cart/index";
    }


    /**
     * 删除购物车中选中的商品
     * @return
     */
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){
        cartFeignClient.deleteChecked();
        return "cart/index";
    }
}
