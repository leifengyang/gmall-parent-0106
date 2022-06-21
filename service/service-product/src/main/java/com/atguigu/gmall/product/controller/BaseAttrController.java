package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {


    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    /**
     * 查询分类下的所有属性名和值
     * @param c1Id 一级分类id；
     * @param c2Id 二级分类id；不传是0
     * @param c3Id 三级分类id；不传是0
     * @return
     */
    @GetMapping("/attrInfoList/{c1id}/{c2id}/{c3id}")
    public Result attrInfoList(@PathVariable("c1id") Long c1Id,
                               @PathVariable("c2id") Long c2Id,
                               @PathVariable("c3id") Long c3Id){


        //查询分类下的所有属性名和值
        List<BaseAttrInfo> infos = baseAttrInfoService.getBaseAttrInfoWithValue(c1Id,c2Id,c3Id);

        return Result.ok(infos);
    }


    /**
     * 保存平台属性/修改二合一
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){

        log.info("保存/修改属性: {}",baseAttrInfo);
        if(baseAttrInfo.getId() != null){
            //修改
            baseAttrInfoService.updateAttrAndValue(baseAttrInfo);
        }else {
            //新增
            baseAttrInfoService.saveAttrAndValue(baseAttrInfo);
        }

        return  Result.ok();
    }


    /**
     * 查询某个属性的所有属性值
     * @param attrId
     * @return
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId")Long attrId){

        List<BaseAttrValue> values = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(values);
    }

}
