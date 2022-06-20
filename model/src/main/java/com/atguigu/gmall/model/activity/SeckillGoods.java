package com.atguigu.gmall.model.activity;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "SeckillGoods")
@TableName("seckill_goods")
public class SeckillGoods extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "spu ID")
	@TableField("spu_id")
	private Long spuId;

	@ApiModelProperty(value = "sku ID")
	@TableField("sku_id")
	private Long skuId;

	@ApiModelProperty(value = "标题")
	@TableField("sku_name")
	private String skuName;

	@ApiModelProperty(value = "商品图片")
	@TableField("sku_default_img")
	private String skuDefaultImg;

	@ApiModelProperty(value = "原价格")
	@TableField("price")
	private BigDecimal price;

	@ApiModelProperty(value = "秒杀价格")
	@TableField("cost_price")
	private BigDecimal costPrice;

	@ApiModelProperty(value = "添加日期")
	@TableField("create_time")
	private Date createTime;

	@ApiModelProperty(value = "审核日期")
	@TableField("check_time")
	private Date checkTime;

	@ApiModelProperty(value = "审核状态")
	@TableField("status")
	private String status;

	@ApiModelProperty(value = "开始时间")
	@TableField("start_time")
	private Date startTime;

	@ApiModelProperty(value = "结束时间")
	@TableField("end_time")
	private Date endTime;

	@ApiModelProperty(value = "秒杀商品数")
	@TableField("num")
	private Integer num;

	@ApiModelProperty(value = "剩余库存数")
	@TableField("stock_count")
	private Integer stockCount;

	@ApiModelProperty(value = "描述")
	@TableField("sku_desc")
	private String skuDesc;

}

