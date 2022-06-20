package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * SpuSaleAttrValue
 * </p>
 *
 */
@Data
@ApiModel(description = "销售属性值")
@TableName("spu_sale_attr_value")
public class SpuSaleAttrValue extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "商品id")
	@TableField("spu_id")
	private Long spuId;

	@ApiModelProperty(value = "销售属性id")
	@TableField("base_sale_attr_id")
	private Long baseSaleAttrId;

	@ApiModelProperty(value = "销售属性值名称")
	@TableField("sale_attr_value_name")
	private String saleAttrValueName;

	@ApiModelProperty(value = "销售属性名称(冗余)")
	@TableField("sale_attr_name")
	private String saleAttrName;

	// 是否是默认选中状态
//	@TableField("sale_attr_name")
//	String isChecked;
	@TableField(exist = false)
	String isChecked;

}

