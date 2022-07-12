package com.atguigu.gmall.order.mapper;


import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


    /**
     *
     * @param id
     * @param userId
     * @param orderStatus    就改为指定的这个状态
     * @param processStatus
     * @param expectStatus   如果订单属于这些状态的某个状态
     * @return
     */
    long updateOrderStatusInExpects(@Param("id") Long id,
                                    @Param("userId") long userId,
                                    @Param("orderStatus") String orderStatus,
                                    @Param("processStatus") String processStatus,
                                    @Param("expectStatus") List<String> expectStatus);

    /**
     * 查询指定订单详情
     * @param orderId
     * @param userId
     * @return
     */
    OrderInfo getOrderInfoAndDetails(@Param("orderId") Long orderId, @Param("userId") Long userId);
}




