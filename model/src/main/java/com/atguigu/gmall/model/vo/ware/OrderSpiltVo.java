package com.atguigu.gmall.model.vo.ware;

import lombok.Data;

@Data
public class OrderSpiltVo {
    private String orderId;
    private String userId; //用户id
    private String wareSkuMap; //json串
}
