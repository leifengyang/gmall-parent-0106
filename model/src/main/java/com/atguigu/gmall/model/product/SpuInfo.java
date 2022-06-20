package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * SpuInfo
 * </p>
 *
 */
@Data
@ApiModel(description = "SpuInfo")
@TableName("spu_info")
public class SpuInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "商品名称")
	@TableField("spu_name")
	private String spuName;

	@ApiModelProperty(value = "商品描述(后台简述）")
	@TableField("description")
	private String description;

	@ApiModelProperty(value = "三级分类id")
	@TableField("category3_id")
	private Long category3Id;

	@ApiModelProperty(value = "品牌id")
	@TableField("tm_id")
	private Long tmId;

	// 销售属性集合
	@TableField(exist = false)
	private List<SpuSaleAttr> spuSaleAttrList;

	// 商品的图片集合
	@TableField(exist = false)
	private List<SpuImage> spuImageList;

}

