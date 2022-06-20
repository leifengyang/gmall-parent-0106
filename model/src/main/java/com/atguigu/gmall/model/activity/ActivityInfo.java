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
@ApiModel(description = "活动信息")
@TableName("activity_info")
public class ActivityInfo extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "活动名称")
   @TableField("activity_name")
   private String activityName;

   @ApiModelProperty(value = "活动类型（满减、折扣）")
   @TableField("activity_type")
   private String activityType;

   @ApiModelProperty(value = "活动描述")
   @TableField("activity_desc")
   private String activityDesc;

   @ApiModelProperty(value = "开始时间")
   @TableField("start_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date startTime;

   @ApiModelProperty(value = "结束时间")
   @TableField("end_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date endTime;

   @ApiModelProperty(value = "创建时间")
   @TableField("create_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;

   // 活动类型
   @TableField(exist = false)
   private String activityTypeString;
}