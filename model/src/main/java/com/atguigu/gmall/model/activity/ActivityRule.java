package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "活动规则")
@TableName("activity_rule")
public class ActivityRule extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "类型")
   @TableField("activity_id")
   private Long activityId;

   @ApiModelProperty(value = "满减金额")
   @TableField("condition_amount")
   private BigDecimal conditionAmount;

   @ApiModelProperty(value = "满减件数")
   @TableField("condition_num")
   private Long conditionNum;

   @ApiModelProperty(value = "优惠金额")
   @TableField("benefit_amount")
   private BigDecimal benefitAmount;

   @ApiModelProperty(value = "优惠折扣")
   @TableField("benefit_discount")
   private BigDecimal benefitDiscount;

   @ApiModelProperty(value = "优惠级别")
   @TableField("benefit_level")
   private Long benefitLevel;

   @ApiModelProperty(value = "活动类型（1：满减，2：折扣）")
   @TableField(exist = false)
   private String activityType;

   // 添加一个skuId
   @ApiModelProperty(value = "活动skuId")
   @TableField(exist = false)
   private Long skuId;

   @ApiModelProperty(value = "优惠后减少金额")
   @TableField(exist = false)
   private BigDecimal reduceAmount;

   @ApiModelProperty(value = "活动对应的skuId列表")
   @TableField(exist = false)
   private List<Long> skuIdList;
}