package com.atguigu.gmall.model.vo.ware;

import com.atguigu.gmall.model.to.mq.WareStockDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrderSplitRespVo {

    private String orderId;
    private String consignee;
    private String consigneeTel;
    private String orderComment;
    private String orderBody;
    private String deliveryAddress;
    private String paymentWay;
    private String wareId;
    private List<WareStockDetail> details;
}
