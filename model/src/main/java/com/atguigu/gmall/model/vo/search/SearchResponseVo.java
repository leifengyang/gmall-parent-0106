package com.atguigu.gmall.model.vo.search;

import com.atguigu.gmall.model.list.Goods;
import lombok.Data;

import java.util.List;

/**
 * 检索完成后的结果响应
 */
@Data
public class SearchResponseVo {


    //此次检索用的所有参数
    private SearchParam searchParam;
    //品牌面包屑
    private String trademarkParam;

    //属性面包屑
    private List<AttrBread>  propsParamList;

    //品牌列表 //品牌的二次检索区展示的内容
    private List<TrademarkSearchVo> trademarkList;

    //属性列表 //属性的二次检索区展示的内容
    private List<AttrSearchVo> attrsList;

    //排序规则
    private OrderMap orderMap;

    //检索到的所有商品
    private List<Goods> goodsList;

    //当前页码
    private Long pageNo;

    //总页码
    private Long totalPages;
}
