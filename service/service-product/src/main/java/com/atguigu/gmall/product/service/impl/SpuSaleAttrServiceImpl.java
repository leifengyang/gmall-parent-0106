package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lfy
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-06-21 09:01:28
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {

        List<SpuSaleAttr> attrs = spuSaleAttrMapper.getSpuSaleAttrList(spuId);

        return attrs;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrAndMarkSkuSaleValue(Long skuId, Long spuId) {

        List<SpuSaleAttr> attrs = spuSaleAttrMapper.getSpuSaleAttrAndMarkSkuSaleValue(skuId,spuId);
        return attrs;
    }
}




