package com.atguigu.gmall.order.mapper;


import com.atguigu.gmall.model.order.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lfy
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-07-08 11:43:31
* @Entity com.atguigu.gmall.order.domain.OrderDetail
*/
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    List<OrderDetail> getOrderDetailsByOrderIdAndUserId(@Param("orderId") Long orderId,
                                                        @Param("userId") Long userId);
}




