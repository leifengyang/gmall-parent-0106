package com.atguigu.gmall.model.vo.search;

import lombok.Data;

import java.util.List;


@Data
public class AttrSearchVo {
    //attrName、attrValueList、attrId
    private Long attrId;
    private String attrName;
    private List<String> attrValueList;
}
