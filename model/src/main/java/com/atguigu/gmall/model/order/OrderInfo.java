package com.atguigu.gmall.model.order;

import com.atguigu.gmall.model.activity.ActivityRule;
import com.atguigu.gmall.model.activity.CouponInfo;
import com.atguigu.gmall.model.base.BaseEntity;
import com.atguigu.gmall.model.enums.ActivityType;
import com.atguigu.gmall.model.enums.CouponType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(description = "订单信息")
@TableName("order_info")
public class OrderInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收货人")
    @TableField("consignee")
    private String consignee;

    @ApiModelProperty(value = "收件人电话")
    @TableField("consignee_tel")
    private String consigneeTel;

    @ApiModelProperty(value = "总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "订单状态")
    @TableField("order_status")
    private String orderStatus;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "付款方式")
    @TableField("payment_way")
    private String paymentWay;

    @ApiModelProperty(value = "送货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty(value = "订单备注")
    @TableField("order_comment")
    private String orderComment;

    @ApiModelProperty(value = "订单交易编号（第三方支付用)")
    @TableField("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "订单描述(第三方支付用)")
    @TableField("trade_body")
    private String tradeBody;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "失效时间")
    @TableField("expire_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    @ApiModelProperty(value = "进度状态")
    @TableField("process_status")
    private String processStatus;

    @ApiModelProperty(value = "物流单编号")
    @TableField("tracking_no")
    private String trackingNo;

    @ApiModelProperty(value = "父订单编号")
    @TableField("parent_order_id")
    private Long parentOrderId;

    @ApiModelProperty(value = "图片路径")
    @TableField("img_url")
    private String imgUrl;

    @TableField(exist = false)
    private List<OrderDetail> orderDetailList;

    @TableField(exist = false)
    private String wareId;

    @ApiModelProperty(value = "地区")
    @TableField("province_id")
    private Long provinceId;

    @ApiModelProperty(value = "促销金额")
    @TableField("activity_reduce_amount")
    private BigDecimal activityReduceAmount;

    @ApiModelProperty(value = "优惠券")
    @TableField("coupon_amount")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "原价金额")
    @TableField("original_total_amount")
    private BigDecimal originalTotalAmount;

    @ApiModelProperty(value = "可退款日期（签收后30天）")
    @TableField("refundable_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date refundableTime;

    @ApiModelProperty(value = "运费")
    @TableField("feight_fee")
    private BigDecimal feightFee;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("operate_time")
    private Date operateTime;

    //  计算活动或者优惠劵的金额
    @TableField(exist = false)
    private List<OrderDetailVo> orderDetailVoList;

    @TableField(exist = false)
    private CouponInfo couponInfo;

    // 计算总价格
    public void sumTotalAmount(){
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal originalTotalAmount = new BigDecimal("0");
        BigDecimal couponAmount = new BigDecimal("0");
        //  减去优惠劵
        if(null != couponInfo) {
            couponAmount = couponAmount.add(couponInfo.getReduceAmount());
            totalAmount = totalAmount.subtract(couponInfo.getReduceAmount());
        }
        //  减去活动
        if(null != this.getActivityReduceAmount()) {
            totalAmount = totalAmount.subtract(this.getActivityReduceAmount());
        }
        //  计算最后
        for (OrderDetail orderDetail : orderDetailList) {
            BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
            originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
            totalAmount = totalAmount.add(skuTotalAmount);
        }
        this.setTotalAmount(totalAmount);
        this.setOriginalTotalAmount(originalTotalAmount);
        this.setCouponAmount(couponAmount);
    }

    /**
     * 获取促销优惠总金额
     * @param orderInfo
     * @return
     */
    public BigDecimal getActivityReduceAmount(OrderInfo orderInfo) {
        //促销优惠金额
        BigDecimal activityReduceAmount = new BigDecimal("0");
        List<OrderDetailVo> orderDetailVoList = orderInfo.getOrderDetailVoList();
        if(!CollectionUtils.isEmpty(orderDetailVoList)) {
            for(OrderDetailVo orderDetailVo : orderDetailVoList) {
                ActivityRule activityRule = orderDetailVo.getActivityRule();
                if(null != activityRule) {
                    activityReduceAmount = activityReduceAmount.add(activityRule.getReduceAmount());
                }
            }
        }
        return activityReduceAmount;
    }
    /**
     * 计算购物项分摊的优惠减少金额
     * 打折：按折扣分担
     * 现金：按比例分摊
     * @param orderInfo
     * @return
     */
    public Map<String, BigDecimal> computeOrderDetailPayAmount(OrderInfo orderInfo) {
        Map<String, BigDecimal> skuIdToReduceAmountMap = new HashMap<>();
        //  获取到订单明细
        List<OrderDetailVo> orderDetailVoList = orderInfo.getOrderDetailVoList();
        if (!CollectionUtils.isEmpty(orderDetailVoList)) {
            for (OrderDetailVo orderDetailVo : orderDetailVoList) {
                //  获取到活动的规则
                ActivityRule activityRule = orderDetailVo.getActivityRule();
                List<OrderDetail> orderDetailList = orderDetailVo.getOrderDetailList();
                if (null != activityRule) {
                    //  key = activity:skuId
                    //优惠金额， 按比例分摊
                    BigDecimal reduceAmount = activityRule.getReduceAmount();
                    if (orderDetailList.size() == 1) {
                        //  记录活动的减少的金额
                        skuIdToReduceAmountMap.put("activity:" + orderDetailList.get(0).getSkuId(), reduceAmount);
                    } else {
                        // 总金额
                        BigDecimal originalTotalAmount = new BigDecimal(0);
                        for (OrderDetail orderDetail : orderDetailList) {
                            BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                            originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                        }
                        //  记录除最后一项是所有分摊金额， 最后一项 = 总的 - skuPartReduceAmount
                        //  三项  100 一项 33.33 二项 33.33 第三项 100-66.66 = 33.34
                        BigDecimal skuPartReduceAmount = new BigDecimal(0);
                        //  促销活动对应的是满减！
                        if (activityRule.getActivityType().equals(ActivityType.FULL_REDUCTION.name())) {
                            for (int i = 0, len = orderDetailList.size(); i < len; i++) {
                                OrderDetail orderDetail = orderDetailList.get(i);
                                //  最后一项前面一项应该如何做
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                                    //sku分摊金额  skuTotalAmount/originalTotalAmount * 优惠的金额
                                    BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                                    skuIdToReduceAmountMap.put("activity:" + orderDetail.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    //  最后一项
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    skuIdToReduceAmountMap.put("activity:" + orderDetail.getSkuId(), skuReduceAmount);
                                }
                            }
                        } else {
                            //  满量打折
                            for (int i = 0, len = orderDetailList.size(); i < len; i++) {
                                OrderDetail orderDetail = orderDetailList.get(i);
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                                    //sku分摊金额
                                    BigDecimal skuDiscountTotalAmount = skuTotalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                                    BigDecimal skuReduceAmount = skuTotalAmount.subtract(skuDiscountTotalAmount);
                                    skuIdToReduceAmountMap.put("activity:" + orderDetail.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    skuIdToReduceAmountMap.put("activity:" + orderDetail.getSkuId(), skuReduceAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
        //  计算优惠券
        CouponInfo couponInfo = orderInfo.getCouponInfo();
        if (null != couponInfo) {
            //sku对应的订单明细
            Map<Long, OrderDetail> skuIdToOrderDetailMap = new HashMap<>();
            for (OrderDetail orderDetail : orderInfo.getOrderDetailList()) {
                skuIdToOrderDetailMap.put(orderDetail.getSkuId(), orderDetail);
            }
            // 优惠券对应的skuId列表
            List<Long> skuIdList = couponInfo.getSkuIdList();
            // 优惠券优惠总金额
            BigDecimal reduceAmount = couponInfo.getReduceAmount();
            if (skuIdList.size() == 1) {
                // sku总的优惠金额 = 优惠券 key = "coupon:skuId"
                skuIdToReduceAmountMap.put("coupon:" + skuIdToOrderDetailMap.get(skuIdList.get(0)).getSkuId(), reduceAmount);
            } else {
                //总金额
                BigDecimal originalTotalAmount = new BigDecimal(0);
                for (Long skuId : skuIdList) {
                    OrderDetail orderDetail = skuIdToOrderDetailMap.get(skuId);
                    BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                    originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                }
                // 记录除最后一项是所有分摊金额， 最后一项 = 总的 - skuPartReduceAmount
                BigDecimal skuPartReduceAmount = new BigDecimal(0);
                // 购物券类型 1 现金券 2 折扣券 3 满减券 4 满件打折券
                if (couponInfo.getCouponType().equals(CouponType.CASH.name()) || couponInfo.getCouponType().equals(CouponType.FULL_REDUCTION.name())) {
                    for (int i = 0, len = skuIdList.size(); i < len; i++) {
                        OrderDetail orderDetail = skuIdToOrderDetailMap.get(skuIdList.get(i));
                        if (i < len - 1) {
                            BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                            //sku分摊金额
                            BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                            skuIdToReduceAmountMap.put("coupon:" + orderDetail.getSkuId(), skuReduceAmount);

                            skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                        } else {
                            BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                            skuIdToReduceAmountMap.put("coupon:" + orderDetail.getSkuId(), skuReduceAmount);
                        }
                    }
                } else {
                    //  对应的2,4
                    for (int i = 0, len = skuIdList.size(); i < len; i++) {
                        OrderDetail orderDetail = skuIdToOrderDetailMap.get(skuIdList.get(i));
                        if (i < len - 1) {
                            BigDecimal skuTotalAmount = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum()));
                            BigDecimal skuDiscountTotalAmount = skuTotalAmount.multiply(couponInfo.getBenefitDiscount().divide(new BigDecimal("10")));
                            BigDecimal skuReduceAmount = skuTotalAmount.subtract(skuDiscountTotalAmount);
                            //sku分摊金额
                            skuIdToReduceAmountMap.put("coupon:" + orderDetail.getSkuId(), skuReduceAmount);

                            skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                        } else {
                            BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                            skuIdToReduceAmountMap.put("coupon:" + orderDetail.getSkuId(), skuReduceAmount);
                        }
                    }
                }
            }
        }
        return skuIdToReduceAmountMap;
    }
}