package com.atguigu.gmall.model.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderSubmitVo {
    private String consignee;
    private String consigneeTel;
    private String deliveryAddress;
    private String orderComment;
    //以上数据要保存到 order_info 表

    //以下数据要保存到 order_detail表
    private List<OrderSubmitDetailVo>  orderDetailList;
}
