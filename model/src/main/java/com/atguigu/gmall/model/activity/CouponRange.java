package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "CouponRange")
@TableName("coupon_range")
public class CouponRange extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "优惠券id")
   @TableField("coupon_id")
   private Long couponId;

   @ApiModelProperty(value = "范围类型")
   @TableField("range_type")
   private String rangeType;

   @ApiModelProperty(value = "rangeId")
   @TableField("range_id")
   private Long rangeId;

}