package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * SpuPoster
 * </p>
 *

 */
@Data
@ApiModel(description = "SpuPoster")
@TableName("spu_poster")
public class SpuPoster extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "商品id")
	@TableField("spu_id")
	private Long spuId;

	@ApiModelProperty(value = "文件名称")
	@TableField("img_name")
	private String imgName;

	@ApiModelProperty(value = "文件路径")
	@TableField("img_url")
	private String imgUrl;

}

