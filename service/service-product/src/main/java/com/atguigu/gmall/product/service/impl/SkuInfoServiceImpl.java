package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.model.vo.user.UserAuth;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.starter.cache.annotation.Cache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SearchFeignClient searchFeignClient;



    static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(4);


    public static void main(String[] args) {
        System.out.println("开始");
        threadPool.schedule(()-> System.out.println("hello"),10,TimeUnit.SECONDS);

        System.out.println("结束");
    }

    @Override
    public void updateSkuInfo(SkuInfo skuInfo){
        //1、改数据库

        //2、双删缓存。
        //1）、立即删   80% 都ok
        redisTemplate.delete(RedisConst.SKU_INFO_CACHE_KEY_PREFIX+skuInfo.getId());
        //2）、延迟删   99.99% 都ok
        //拿到一个延迟任务的线程池
        threadPool.schedule(()->redisTemplate.delete(RedisConst.SKU_INFO_CACHE_KEY_PREFIX+skuInfo.getId()),10, TimeUnit.SECONDS);
        //立即结束
        //兜底：数据有过期时间。 redis怎么删数据？
        //redis怎么淘汰这些过期数据？
        //1）、
    }




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
        //1、数据库修改状态
        skuInfoMapper.updateSaleStatus(skuId,1);
        //2、数据保存到es中
        Goods goods = this.getGoodsInfoBySkuId(skuId);
        //3、远程调用检索服务进行上架
        searchFeignClient.upGoods(goods);

    }

    @Override
    public void downSku(Long skuId) {
        //1、修改数据库状态
        skuInfoMapper.updateSaleStatus(skuId,0);

        //2、链接es远程下架
        searchFeignClient.downGoods(skuId);
    }

    @Cache(
            key=RedisConst.SKU_PRICE_CACHE_PREFIX+"#{#params[0]}",
            bloomName = RedisConst.SKU_BLOOM_FILTER_NAME,
            bloomIf = "#{#params[0]}"
    )
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return skuInfoMapper.getSkuPrice(skuId);
    }

    @Override
    public List<Long> getSkuIds() {
        //改造为分批分页查询。
        return skuInfoMapper.getSkuIds();
    }




    @Override
    public Goods getGoodsInfoBySkuId(Long skuId) {
        //es中一个sku的详情。对应一个 Goods

        Goods goods = skuInfoMapper.getGoodsInfoBySkuId(skuId);
        return goods;
    }

    @Override
    public CartInfo getCartInfoBySkuId(Long skuId) {
        CartInfo cartInfo = new CartInfo();
        UserAuth userAuth = AuthContextHolder.getUserAuth();
        if(userAuth.getUserId()!=null){
            cartInfo.setUserId(userAuth.getUserId().toString());
        }else {
            cartInfo.setUserId(userAuth.getTempId());
        }

        cartInfo.setSkuId(skuId);
        cartInfo.setId(cartInfo.getId());

        //查价格
        BigDecimal skuPrice = skuInfoMapper.getSkuPrice(skuId);
        cartInfo.setCartPrice(skuPrice);

        cartInfo.setSkuNum(null);

        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());
        //默认被选中
        cartInfo.setIsChecked(1);

        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());

        //商品实时价格
        cartInfo.setSkuPrice(skuPrice);  //未来价格发生变动的话，需要改购物车

        cartInfo.setCouponInfoList(null);


        return cartInfo;
    }

    @Override
    public BigDecimal get1010SkuPrice(Long skuId) {
        return skuInfoMapper.getSkuPrice(skuId);
    }
}




