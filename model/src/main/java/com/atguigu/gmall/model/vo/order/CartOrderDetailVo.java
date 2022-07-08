package com.atguigu.gmall.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartOrderDetailVo {
    //imgUrl、skuName、orderPrice、skuNum、stock
    private String imgUrl;
    private String skuName;
    private BigDecimal orderPrice;
    private Integer skuNum;
    private String stock; //1有货、无货0
}
