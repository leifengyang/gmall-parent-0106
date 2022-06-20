package com.atguigu.gmall.model.user;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户地址")
@TableName("user_address")
public class UserAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户地址")
    @TableField("user_address")
    private String userAddress;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "收件人")
    @TableField("consignee")
    private String consignee;

    @ApiModelProperty(value = "联系方式")
    @TableField("phone_num")
    private String phoneNum;

    @ApiModelProperty(value = "是否是默认")
    @TableField("is_default")
    private String isDefault;

}

