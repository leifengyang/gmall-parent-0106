package com.atguigu.gmall.feign.ware;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//value是必须的，只要有url存在的情况下，请求给url指定的绝对路径发请求，
//value只是作为客户端的名字（未来用来对这个客户端定制化配置）
@FeignClient(url="http://localhost:9001",value = "ware-manage")
public interface WareFeignClient {
    //http://localhost:9001/hasStock?skuId=43&num=1
    @GetMapping("/hasStock")
    public String hasStock(@RequestParam("skuId") Long skuId,
                           @RequestParam("num") Integer num);
}
