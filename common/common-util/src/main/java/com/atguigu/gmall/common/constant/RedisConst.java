package com.atguigu.gmall.common.constant;

public class RedisConst {


    public static final String SKU_INFO_CACHE_KEY_PREFIX = "sku:info:";


    public static final String SKU_INFO_LOCK_PREFIX = "lock:sku:info:";

    public static final long SKU_INFO_CACHE_TIMEOUT = 1000*60*60*24*7L; //ms为单位
    public static final String SKU_BLOOM_FILTER_NAME = "bloom:skuid";
    public static final String LOCK_PREFIX = "lock:";
}
