package com.atguigu.gmall.ware.bean;


import com.atguigu.gmall.ware.bean.enums.OrderStatus;
import com.atguigu.gmall.ware.bean.enums.ProcessStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private String id;

     @TableField
    private String consignee;

    @TableField
    private String consigneeTel;

    @TableField
    private BigDecimal totalAmount;

     @TableField
    private OrderStatus orderStatus;

    @TableField
    private ProcessStatus processStatus;

    @TableField
    private String userId;


     @TableField
    private String deliveryAddress;

     @TableField
    private String orderComment;

     @TableField
    private Date createTime;

     @TableField
     private String parentOrderId;

     @TableField
     private String trackingNo;

    @TableField(exist = false)
     private List<OrderDetail> orderDetailList;

    @TableField(exist = false)
     private List<OrderInfo> orderSubList;

    @TableField(exist = false)
     private String wareId;

     @TableField
     private String outTradeNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }



    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }



    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress == null ? null : deliveryAddress.trim();
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }


    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getOrderBoby(){
        String body="";
        if(orderDetailList!=null&&orderDetailList.size()>0){
            body=  orderDetailList.get(0).getSkuName();
        }
        body+="等"+getSumSkuNums()+"件商品";
        return body;

    }

    public Integer getSumSkuNums(){
        Integer sumNums=0;
        for (OrderDetail orderDetail : orderDetailList) {
            sumNums+=  orderDetail.getSkuNum();
        }
        return sumNums;
    }

    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public List<OrderInfo> getOrderSubList() {
        return orderSubList;
    }

    public void setOrderSubList(List<OrderInfo> orderSubList) {
        this.orderSubList = orderSubList;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}