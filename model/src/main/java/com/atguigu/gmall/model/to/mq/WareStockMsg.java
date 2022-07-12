package com.atguigu.gmall.model.to.mq;


import lombok.Data;

import java.util.List;

@Data
public class WareStockMsg {

    private Long orderId;
    private Long userId;
    private String consignee;
    private String consigneeTel;
    private String orderComment;
    private String orderBody;
    private String deliveryAddress;
    private String paymentWay = "2";// "1,2"
    private List<WareStockDetail> details;

}
