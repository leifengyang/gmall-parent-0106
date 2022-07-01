package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author lfy
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-06-21 09:01:27
*/
public interface SkuInfoService extends IService<SkuInfo> {

    /**
     * 保存skuInfo
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 上架
     * @param skuId
     */
    void upSku(Long skuId);

    /**
     * 下架
     * @param skuId
     */
    void downSku(Long skuId);

    /**
     * 查询价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 获取所有id
     * @return
     */
    List<Long> getSkuIds();


    /**
     * 修改sku
     * @param skuInfo
     */
    public void updateSkuInfo(SkuInfo skuInfo);

    /**
     * 根据skuId，得到商品的完整信息，封装成es需要的模型
     * @param skuId
     * @return
     */
    Goods getGoodsInfoBySkuId(Long skuId);
}
