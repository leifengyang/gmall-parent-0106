package com.atguigu.gmall.model.list;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 总的数据
@Data
public class SearchResponseVo implements Serializable {

    //品牌 此时vo对象中的id字段保留（不用写） name就是“品牌” value: [{id:100,name:华为,logo:xxx},{id:101,name:小米,log:yyy}]
    private List<SearchResponseTmVo> trademarkList;
    //所有商品的顶头显示的筛选属性
    private List<SearchResponseAttrVo> attrsList = new ArrayList<>();

    //检索出来的商品信息
    private List<Goods> goodsList = new ArrayList<>();

    private Long total;//总记录数
    private Integer pageSize;//每页显示的内容
    private Integer pageNo;//当前页面
    private Long totalPages;

}
