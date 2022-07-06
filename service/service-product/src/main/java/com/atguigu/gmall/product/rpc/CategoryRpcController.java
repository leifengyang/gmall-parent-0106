package com.atguigu.gmall.product.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.CategoryVo;
import com.atguigu.gmall.product.biz.CategoryBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC暴露所有和分类有关的远程接口
 * 1、所有远程调用请求  /rpc/inner/product/调用路径
 *  /rpc/inner/微服务名/路径
 */
@RequestMapping("/rpc/inner/product")
@RestController
public class CategoryRpcController {


    @Autowired
    CategoryBizService categoryBizService;


    //缓存来加速系统；
    //缓存一个key，值只是不一样。原来key指向的对象，由于断了引用，会被GC回收。
    Map<String,List<CategoryVo>>  categoryCache = new ConcurrentHashMap<>();


    /**
     * 获取系统所有三级分类并组装成树形结构
     * 把对象自动序列化成json数据。
     * 序列化：   把内存中的 JavaBean 表示成一个实际的文本或者二进制文件方式。 为了传输或者持久化
     * 反序列化：  把之前传输过来或者从磁盘中读取到的文本或文件，转为内存中的JavaBean。 为了方便编码使用。
     *
     * RPC远程调用？
     *
     * @return
     */
//    @GetMapping("/categorys/all")
//    public Result<List<CategoryVo>> getCategorysLocalCache(){
//        //1、先看有之前缓存的数据
//        List<CategoryVo> cacheData = categoryCache.get("categorys");
//        if(cacheData!=null){
//            //2、缓存中有。
//            return Result.ok(cacheData);
//        }
//
//        //3、缓存没有：去数据库查询所有三级分类
//        List<CategoryVo> vos =   categoryBizService.getCategorys();
//        //4、查询到以后放到缓存
//        categoryCache.put("categorys",vos);
//        return Result.ok(vos);
//    }


    @GetMapping("/categorys/all")
    public Result<List<CategoryVo>> getCategorysRedis() throws InterruptedException {
        System.out.println("正在获取菜单...");
//        Thread.sleep(2000);
        List<CategoryVo> vos =   categoryBizService.getCategorys();

        return Result.ok(vos);
    }
}
