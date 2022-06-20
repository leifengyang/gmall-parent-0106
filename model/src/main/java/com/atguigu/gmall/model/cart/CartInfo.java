package com.atguigu.gmall.model.cart;

import com.atguigu.gmall.model.activity.CouponInfo;
import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@ApiModel(description = "购物车")
public class CartInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "skuid")
    @TableField("sku_id")
    private Long skuId;

    @ApiModelProperty(value = "放入购物车时价格")
    @TableField("cart_price")
    private BigDecimal cartPrice;

    @ApiModelProperty(value = "数量")
    @TableField("sku_num")
    private Integer skuNum;

    @ApiModelProperty(value = "图片文件")
    @TableField("img_url")
    private String imgUrl;

    @ApiModelProperty(value = "sku名称 (冗余)")
    @TableField("sku_name")
    private String skuName;

    @ApiModelProperty(value = "isChecked")
    @TableField("is_checked")
    private Integer isChecked = 1;

    //  ,fill = FieldFill.INSERT
    @TableField(value = "create_time")
    private Timestamp createTime;

    //  ,fill = FieldFill.INSERT_UPDATE)
    @TableField(value = "update_time")
    private Timestamp updateTime;

    // 实时价格 skuInfo.price
    @TableField(exist = false)
    BigDecimal skuPrice;

    //  优惠券信息列表
    @ApiModelProperty(value = "购物项对应的优惠券信息")
    @TableField(exist = false)
    private List<CouponInfo> couponInfoList;

}
