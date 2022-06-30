package com.atguigu.gmall.product;


import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class ReadWriteSpTest {
    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageMapper skuImageMapper;



//    @Transactional  数据一致性非常高 ACID
    @Test
    public void testReadWrite(){
        SkuImage image = new SkuImage();

        image.setImgName("a");
        image.setImgUrl("b");
        image.setSpuImgId(1L);
        image.setIsDefault("1");

        skuImageMapper.insert(image);

        //强制使用主库；
        HintManager.getInstance()
                .setWriteRouteOnly();
        //读取 如果是一个事务内，强制把读发给主库
        SkuImage skuImage = skuImageMapper.selectById(image.getId());
        System.out.println("刚才插入的是："+skuImage);

    }

    @Test
    public void testReadLb(){
        for (int i = 0; i < 2; i++) {
            BigDecimal price = skuInfoMapper.getSkuPrice(49L);
            System.out.println(price);
        }

    }
}
