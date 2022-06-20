package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "活动sku范围")
@TableName("activity_sku")
public class ActivitySku extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "活动id ")
   @TableField("activity_id")
   private Long activityId;

   @ApiModelProperty(value = "sku_id")
   @TableField("sku_id")
   private Long skuId;

   @ApiModelProperty(value = "创建时间")
   @TableField("create_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;

}