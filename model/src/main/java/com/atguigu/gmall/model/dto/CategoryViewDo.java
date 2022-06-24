package com.atguigu.gmall.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("category_view")
@Data
public class CategoryViewDo {

    @TableField("id")
    private Long id;

    @TableField("name")
    private String name;
    @TableField("c2Id")
    private Long  c2id;
    @TableField("c2name")
    private String c2name;
    @TableField("c3id")
    private Long c3id;
    @TableField("c3name")
    private String c3name;

}
