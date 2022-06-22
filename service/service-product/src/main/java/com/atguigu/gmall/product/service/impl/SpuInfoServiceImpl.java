package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author lfy
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-06-21 09:01:28
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //1、id，spuName，description，category3Id，tmId保存到spu_info
        //保存spu_info
        save(spuInfo);
        Long id = spuInfo.getId();

        //2、spuImageList保存到spu_image
        List<SpuImage> imageList = spuInfo.getSpuImageList();
        for (SpuImage image : imageList) {
            image.setSpuId(id); //回填spuId
        }
        spuImageService.saveBatch(imageList);

        //3、spuSaleAttrList 保存到  spu_sale_attr
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(id);
            //保存spu的销售属性名
            spuSaleAttrService.save(spuSaleAttr);

            //提取spu的销售属性值集合
            List<SpuSaleAttrValue> valueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : valueList) {
                //填补信息
                value.setSpuId(id);
                value.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                //保存销售属性值到spu_sale_attr_value表中
                spuSaleAttrValueService.save(value);
            }
        }
    }
}




