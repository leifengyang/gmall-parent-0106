package com.atguigu.gmall.model.cart;

import com.atguigu.gmall.model.activity.ActivityRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CartInfoVo implements Serializable {
   
   private static final long serialVersionUID = 1L;

   /**
    * 购物车中哪些skuId对应同一组活动规则
    * 如：skuId为1与2的购物项  对应  活动1的规则 （满1000减100 满2000减200）
    */
   // 不同的skuId 对应相同的活动
   @ApiModelProperty(value = "cartInfoList")
   private List<CartInfo> cartInfoList;

   // activityRuleList 存储的是同一个活动规则列表
   @ApiModelProperty(value = "活动规则列表")
   private List<ActivityRule> activityRuleList;

}