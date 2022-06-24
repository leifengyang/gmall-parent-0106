package com.atguigu.gmall.feign.product;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 *   @FeignClient 代表这会是一次远程调用
 */
@RequestMapping("/rpc/inner/product")
@FeignClient("service-product")  //声明需要调用的微服务
public interface CategoryFeignClient {


    /**
     * 1、只要调用 CategoryFeignClient.getCategorys();
     * feign就知道：
     *  1）、需要给 service-product 服务发送请求
     *  2）、feign找nacos要到 service-product 服务的地址。
     *  3）、feign准备一个url开始发送请求。
     *       http://从nacos查到的service-product的ip:port/rpc/inner/product/categorys/all
     *       http://192.168.15.1:7000/rpc/inner/product/categorys/all
     *  4）、远程收到请求就执行，并返回数据，远程响应json数据。
     *  5）、feign的方法接到json数据。feign把接受到的这个json转为指定的类型 Result<List<CategoryVo>>
     *  6）、或者写成兼容类型都可以。Map<String,Object>
     *
     * @return
     */
    @GetMapping("/categorys/all")
    Result<List<CategoryVo>> getCategorys();

}
