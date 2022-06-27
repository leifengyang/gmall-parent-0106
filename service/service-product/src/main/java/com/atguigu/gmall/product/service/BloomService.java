package com.atguigu.gmall.product.service;

public interface BloomService {
    void initBloom();

    /**
     * 重建sku布隆
     */
    void rebuildSkuBloom();

}
