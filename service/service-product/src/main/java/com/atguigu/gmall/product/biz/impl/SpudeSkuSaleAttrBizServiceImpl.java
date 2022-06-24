package com.atguigu.gmall.product.biz.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.vo.ValueSkuVo;
import com.atguigu.gmall.product.biz.SpudeSkuSaleAttrBizService;
import com.atguigu.gmall.product.mapper.SkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpudeSkuSaleAttrBizServiceImpl implements SpudeSkuSaleAttrBizService {


    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public String getSpudeAllSkuSaleAttrAndValue(Long spuId) {

        List<ValueSkuVo> skuVo =  skuSaleAttrValueMapper.getSpudeAllSkuSaleAttrAndValue(spuId);
        Map<String,String> jsonMap = new HashMap<>();
        for (ValueSkuVo vo : skuVo) {
            String values = vo.getSku_values();
            String skuId = vo.getSku_id();
            jsonMap.put(values,skuId);
        }

        // { "115|117":"44","114|117":"45","114|116":"48" }
        return JSONs.toStr(jsonMap);
    }
}
