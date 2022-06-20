package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * SkuSaleAttrValue
 * </p>
 *
 */
@Data
@ApiModel(description = "Sku销售属性值")
@TableName("sku_sale_attr_value")
public class SkuSaleAttrValue extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "库存单元id")
	@TableField("sku_id")
	private Long skuId;

	@ApiModelProperty(value = "spu_id(冗余)")
	@TableField("spu_id")
	private Long spuId;

	@ApiModelProperty(value = "销售属性值id")
	@TableField("sale_attr_value_id")
	private Long saleAttrValueId;

}

