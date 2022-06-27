package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
* @author lfy
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-06-21 09:01:27
*/
@Slf4j
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    RedissonClient redissonClient;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        log.info("sku信息正在保存：{}",skuInfo);
        //1、保存sku的基本信息
        save(skuInfo);
        Long skuId = skuInfo.getId();

        //2、保存sku图片
        List<SkuImage> imageList = skuInfo.getSkuImageList();
        for (SkuImage image : imageList) {
            image.setSkuId(skuId);
        }
        skuImageService.saveBatch(imageList);

        //3、保存sku的平台属性
        List<SkuAttrValue> attrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue value : attrValueList) {
            value.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(attrValueList);

        //4、保存sku的销售属性
        List<SkuSaleAttrValue> saleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue attrValue : saleAttrValueList) {
            attrValue.setSkuId(skuId);
            attrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(saleAttrValueList);

        log.info("sku信息保存完成：生成的skuId：{}",skuId);

        //添到布隆过滤器中
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);
        filter.add(skuId);

    }

    @Override
    public void upSku(Long skuId) {
        //TODO 连接ES保存这个商品数据
        skuInfoMapper.updateSaleStatus(skuId,1);
    }

    @Override
    public void downSku(Long skuId) {
        //TODO 连接ES删除这个商品数据
        skuInfoMapper.updateSaleStatus(skuId,0);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {

        return skuInfoMapper.getSkuPrice(skuId);
    }

    @Override
    public List<Long> getSkuIds() {
        //改造为分批分页查询。
        return skuInfoMapper.getSkuIds();
    }
}




