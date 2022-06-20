package com.atguigu.gmall.model.product;

import com.atguigu.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * BaseAttrValue
 * </p>
 *
 */
@Data
@ApiModel(description = "平台属性值")
@TableName("base_attr_value")
public class BaseAttrValue extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "属性值名称")
	@TableField("value_name")
	private String valueName;

	@ApiModelProperty(value = "属性id")
	@TableField("attr_id")
	private Long attrId;
}

