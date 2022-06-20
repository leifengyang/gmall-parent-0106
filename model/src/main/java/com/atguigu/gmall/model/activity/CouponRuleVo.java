package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.enums.CouponRangeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "优惠券规则")
public class CouponRuleVo implements Serializable {
   
   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value = "优惠券id")
   private Long couponId;

   @ApiModelProperty(value = "范围类型")
   private CouponRangeType rangeType;

   @ApiModelProperty(value = "满额数")
   private BigDecimal conditionAmount;

   @ApiModelProperty(value = "满件数")
   private Long conditionNum;

   @ApiModelProperty(value = "减金额")
   private BigDecimal benefitAmount;

   @ApiModelProperty(value = "折扣")
   private BigDecimal benefitDiscount;

   @ApiModelProperty(value = "优惠券参与的商品list")
   private List<CouponRange> couponRangeList;

   @ApiModelProperty(value = "优惠券范围描述")
   private String rangeDesc;

}