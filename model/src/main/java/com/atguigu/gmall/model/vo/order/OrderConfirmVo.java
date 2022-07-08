package com.atguigu.gmall.model.vo.order;

import com.atguigu.gmall.model.user.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认数据
 */
@Data
public class OrderConfirmVo {
    private List<UserAddress> userAddressList;


    private List<CartOrderDetailVo> detailArrayList;

    private Integer totalNum;
    private BigDecimal totalAmount;
    private String tradeNo;//追踪号，防重令牌
}
