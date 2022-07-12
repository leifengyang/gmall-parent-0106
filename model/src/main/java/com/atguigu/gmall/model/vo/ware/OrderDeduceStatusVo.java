package com.atguigu.gmall.model.vo.ware;


import lombok.Data;

@Data
public class OrderDeduceStatusVo {
    private Long orderId;
    private Long userId;
    private String status;
}
