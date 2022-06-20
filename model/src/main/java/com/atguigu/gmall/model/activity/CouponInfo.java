package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "CouponInfo")
@TableName("coupon_info")
public class CouponInfo extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "购物券名称")
   @TableField("coupon_name")
   private String couponName;

   @ApiModelProperty(value = "购物券类型")
   @TableField("coupon_type")
   private String couponType;

   @ApiModelProperty(value = "满额数")
   @TableField("condition_amount")
   private BigDecimal conditionAmount;

   @ApiModelProperty(value = "满件数")
   @TableField("condition_num")
   private Long conditionNum;

   @ApiModelProperty(value = "活动编号")
   @TableField("activity_id")
   private Long activityId;

   @ApiModelProperty(value = "减金额")
   @TableField("benefit_amount")
   private BigDecimal benefitAmount;

   @ApiModelProperty(value = "折扣")
   @TableField("benefit_discount")
   private BigDecimal benefitDiscount;

   @ApiModelProperty(value = "范围类型")
   @TableField("range_type")
   private String rangeType;

   @ApiModelProperty(value = "最多领用次数")
   @TableField("limit_num")
   private Integer limitNum;

   @ApiModelProperty(value = "已领用次数")
   @TableField("taken_count")
   private Integer takenCount;

   @ApiModelProperty(value = "可以领取的开始日期")
   @TableField("start_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date startTime;

   @ApiModelProperty(value = "可以领取的结束日期")
   @TableField("end_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date endTime;

   @ApiModelProperty(value = "创建时间")
   @TableField("create_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;

   @ApiModelProperty(value = "修改时间")
   @TableField("operate_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date operateTime;

   @ApiModelProperty(value = "过期时间")
   @TableField("expire_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date expireTime;

   @ApiModelProperty(value = "优惠券范围描述")
   @TableField("range_desc")
   private String rangeDesc;

   @TableField(exist = false)
   private String couponTypeString;

   @TableField(exist = false)
   private String rangeTypeString;

   @ApiModelProperty(value = "是否领取")
   @TableField(exist = false)
   private Integer isGet;

   @ApiModelProperty(value = "购物券状态（1：未使用 2：已使用）")
   @TableField(exist = false)
   private String couponStatus;

   @ApiModelProperty(value = "范围类型id")
   @TableField(exist = false)
   private Long rangeId;

   @ApiModelProperty(value = "优惠券对应的skuId列表")
   @TableField(exist = false)
   private List<Long> skuIdList;

   @ApiModelProperty(value = "优惠后减少金额")
   @TableField(exist = false)
   private BigDecimal reduceAmount;

   @ApiModelProperty(value = "是否最优选项")
   @TableField(exist = false)
   private Integer isChecked = 0;

   @ApiModelProperty(value = "是否可选")
   @TableField(exist = false)
   private Integer isSelect = 0;

}