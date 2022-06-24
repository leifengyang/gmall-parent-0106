package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
