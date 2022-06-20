package com.atguigu.gmall.model.activity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRecode implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long skuId;
	
	private String userId;
}
