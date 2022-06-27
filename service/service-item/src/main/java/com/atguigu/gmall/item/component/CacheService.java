package com.atguigu.gmall.item.component;

import java.util.concurrent.TimeUnit;

public interface CacheService {
    <T>T getData(String cacheKey, Class<T> t);

    <T> void saveData(String cacheKey, T detail);

    <T> void saveData(String cacheKey, T detail, Long time, TimeUnit unit);
}
