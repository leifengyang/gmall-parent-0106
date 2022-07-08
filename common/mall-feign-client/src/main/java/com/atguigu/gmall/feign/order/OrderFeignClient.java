package com.atguigu.gmall.feign.order;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/rpc/inner/order")
@FeignClient("service-order")
public interface OrderFeignClient {

    /**
     * 获取订单确认数据
     * @return
     */
    @GetMapping("/confirm/data")
    Result<OrderConfirmVo> getOrderConfirmData();

}
