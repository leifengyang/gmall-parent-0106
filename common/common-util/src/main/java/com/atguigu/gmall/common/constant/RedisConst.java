package com.atguigu.gmall.common.constant;

public class RedisConst {


    public static final String SKU_INFO_CACHE_KEY_PREFIX = "sku:info:";


    public static final String SKU_INFO_LOCK_PREFIX = "lock:sku:info:";

    public static final long SKU_INFO_CACHE_TIMEOUT = 1000*60*60*24*7L; //ms为单位
    public static final String SKU_BLOOM_FILTER_NAME = "bloom:skuid";
    public static final String LOCK_PREFIX = "lock:";
    public static final String SKU_HOTSCORE = "hotscore:";
    public static final String USER_LOGIN_PREFIX = "user:login:";
    public static final String CART_INFO_PREFIX = "cart:info:";
    public static final Integer CART_SIZE_LIMIT = 200;
    public static final String SKU_PRICE_CACHE_PREFIX = "sku:price:"; //sku:price:49
    public static final String TRADE_TOKEN_PREFIX = "trade:token:"; //交易防重令牌
    public static final String A_KEN_VALUE = "x"; //一个占坑用的值
}
