package com.atguigu.gmall.model.to.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WareStockDetail {
    private Long skuId;
    private Integer skuNum;
    private String skuName;
}
