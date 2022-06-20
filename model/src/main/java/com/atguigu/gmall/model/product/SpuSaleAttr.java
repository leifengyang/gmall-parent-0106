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
 * SpuSaleAttr
 * </p>
 *
 */
@Data
@ApiModel(description = "销售属性")
@TableName("spu_sale_attr")
public class SpuSaleAttr extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "商品id")
	@TableField("spu_id")
	private Long spuId;

	@ApiModelProperty(value = "销售属性id")
	@TableField("base_sale_attr_id")
	private Long baseSaleAttrId;

	@ApiModelProperty(value = "销售属性名称(冗余)")
	@TableField("sale_attr_name")
	private String saleAttrName;

	// 销售属性值对象集合
	@TableField(exist = false)
	List<SpuSaleAttrValue> spuSaleAttrValueList;

}

