package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * SkuImage
 * </p>
 *
 */
@Data
@ApiModel(description = "Sku图片")
@TableName("sku_image")
public class SkuImage extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "商品id")
	@TableField("sku_id")
	private Long skuId;

	@ApiModelProperty(value = "图片名称（冗余）")
	@TableField("img_name")
	private String imgName;

	@ApiModelProperty(value = "图片路径(冗余)")
	@TableField("img_url")
	private String imgUrl;

	@ApiModelProperty(value = "商品图片id")
	@TableField("spu_img_id")
	private Long spuImgId;

	@ApiModelProperty(value = "是否默认")
	@TableField("is_default")
	private String isDefault;

}

