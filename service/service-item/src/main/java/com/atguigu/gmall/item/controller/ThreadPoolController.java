package com.atguigu.gmall.item.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class ThreadPoolController {

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    //关池
    @GetMapping("/pool/close")
    public String closePool(){
        threadPoolExecutor.shutdown();
        return "";
    }

    //返回指标
    @GetMapping("/pool/metrics")
    public Map metrics(){
        threadPoolExecutor.getCorePoolSize();
        return null;
    }
}
