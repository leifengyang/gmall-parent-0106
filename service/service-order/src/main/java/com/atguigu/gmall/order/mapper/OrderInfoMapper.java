package com.atguigu.gmall.order.mapper;


import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author lfy
* @description 针对表【order_info(订单表 订单表)】的数据库操作Mapper
* @createDate 2022-07-08 11:43:31
* @Entity com.atguigu.gmall.order.domain.OrderInfo
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     *
     * @param orderId
     * @param userId
     * @param orderStatus
     * @param processStatus  则改为以上这些状态
     * @param expectStatus   如果订单是这个状态
     */
    long updateOrderStatus(@Param("orderId") Long orderId,
                           @Param("userId") Long userId,
                           @Param("orderStatus") String orderStatus,
                           @Param("processStatus") String processStatus,
                           @Param("expectStatus") String expectStatus);
}




