package com.atguigu.gmall.starter.cache.component;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.TimeUnit;

public interface CacheService {
    //简单类型用这个
    <T>T getData(String cacheKey, Class<T> t);

    //复杂类型用这个
    <T>T getData(String cacheKey, TypeReference<T> t);

    <T> void saveData(String cacheKey, T detail);

    <T> void saveData(String cacheKey, T detail, Long time, TimeUnit unit);
}
