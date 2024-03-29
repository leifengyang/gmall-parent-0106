package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.model.vo.ValueSkuVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lfy
* @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Mapper
* @createDate 2022-06-21 09:01:28
* @Entity com.atguigu.gmall.product.domain.SkuSaleAttrValue
*/
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {


    List<ValueSkuVo> getSpudeAllSkuSaleAttrAndValue(@Param("spuId") Long spuId);
}




