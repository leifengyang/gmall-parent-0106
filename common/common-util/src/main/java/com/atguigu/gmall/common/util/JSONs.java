package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JSONs {
    static ObjectMapper mapper = new ObjectMapper();
    /**
     * 对象转json
     * @param o
     * @return
     */
    public static String toStr(Object o){
        try {
           return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static<T> T toObj(String json, TypeReference<T> ref) {
        T t = null;
        try {
            t = mapper.readValue(json, ref);
        } catch (JsonProcessingException e) {
            log.error("json转换对象异常：{}",e);
        }
        return t;
    }
}
