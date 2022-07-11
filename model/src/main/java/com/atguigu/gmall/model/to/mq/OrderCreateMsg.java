package com.atguigu.gmall.model.to.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreateMsg {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;

}
