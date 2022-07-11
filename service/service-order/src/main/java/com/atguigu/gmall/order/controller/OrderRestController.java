package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderBizService;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order/auth")
public class OrderRestController {


    @Autowired
    OrderBizService orderBizService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderDetailService orderDetailService;
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


    /**
     * 分页获取用户的所有订单
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/{pageNo}/{pageSize}")
    public Result orderList(@PathVariable("pageNo") Integer pageNo,
                            @PathVariable("pageSize") Integer pageSize){

        Page<OrderInfo> page = new Page<>(pageNo,pageSize);
        page.addOrder(OrderItem.desc("id"));

        Long userId = AuthContextHolder.getUserAuth().getUserId();
        QueryWrapper<OrderInfo> infowrapper = new QueryWrapper<>();
        infowrapper.eq("user_id",userId);

        Page<OrderInfo> result = orderInfoService.page(page,infowrapper);

        List<OrderInfo> infos = result.getRecords().stream().map(record -> {
            //查出订单详情
            QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("order_id", record.getId());
            List<OrderDetail> list = orderDetailService.list(wrapper);
            record.setOrderDetailList(list);
            return record;
        }).collect(Collectors.toList());

        result.setRecords(infos);


        return Result.ok(result);
    }
}
