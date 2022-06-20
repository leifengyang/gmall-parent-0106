package com.atguigu.gmall.model.order;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "订单活动关联表")
@TableName("order_detail_activity")
public class OrderDetailActivity extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "订单id")
   @TableField("order_id")
   private Long orderId;

   @ApiModelProperty(value = "订单明细id")
   @TableField("order_detail_id")
   private Long orderDetailId;

   @ApiModelProperty(value = "活动ID")
   @TableField("activity_id")
   private Long activityId;

   @ApiModelProperty(value = "活动规则")
   @TableField("activity_rule")
   private Long activityRule;

   @ApiModelProperty(value = "skuID")
   @TableField("sku_id")
   private Long skuId;

   @ApiModelProperty(value = "创建时间")
   @TableField("create_time")
   private Date createTime;

}