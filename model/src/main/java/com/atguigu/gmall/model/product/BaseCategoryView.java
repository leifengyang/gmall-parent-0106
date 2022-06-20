//
//
package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * BaseCategoryView
 * </p>
 *
 */
@Data
@ApiModel(description = "BaseCategoryView")
@TableName("base_category_view")
public class BaseCategoryView extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "一级分类编号")
	@TableField("category1_id")
	private Long category1Id;

	@ApiModelProperty(value = "一级分类名称")
	@TableField("category1_name")
	private String category1Name;

	@ApiModelProperty(value = "二级分类编号")
	@TableField("category2_id")
	private Long category2Id;

	@ApiModelProperty(value = "二级分类名称")
	@TableField("category2_name")
	private String category2Name;

	@ApiModelProperty(value = "三级分类编号")
	@TableField("category3_id")
	private Long category3Id;

	@ApiModelProperty(value = "三级分类名称")
	@TableField("category3_name")
	private String category3Name;

}

