package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lfy
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-07-08 11:43:31
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Override
    public List<OrderDetail> getOrderDetailsByOrderIdAndUserId(Long orderId, Long userId) {

        return  orderDetailMapper.getOrderDetailsByOrderIdAndUserId(orderId,userId);
    }
}




