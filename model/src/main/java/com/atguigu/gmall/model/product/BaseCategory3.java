package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * BaseCategory3
 * </p>
 *
 */
@Data
@ApiModel(description = "商品三级分类")
@TableName("base_category3")
public class BaseCategory3 extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "三级分类名称")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "二级分类编号")
	@TableField("category2_id")
	private Long category2Id;

}

