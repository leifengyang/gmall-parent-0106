package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/auth")
public class OrderRestController {


    @Autowired
    OrderBizService orderBizService;
    /**
     * 提交订单
     * @param tradeNo
     * @param order
     * @return
     */
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo order){

        //创建订单，并返回订单Id（字符串）  //89808052804589000。
        Long id = orderBizService.submitOrder(tradeNo,order);

        return Result.ok(id+"");
    }

}
