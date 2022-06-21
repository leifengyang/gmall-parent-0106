package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌功能控制器
 */
@RequestMapping("/admin/product")
@RestController
public class TrademarkController {


    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * page：第几页
     * limit：每页数量
     * @return
     */
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result baseTrademark(@PathVariable("page")  Long page,
                                @PathVariable("limit") Long limit){

        Page<BaseTrademark> p = new Page<>(page,limit);

        //分页查询
        Page<BaseTrademark> result = baseTrademarkService.page(p);

        return Result.ok(result);
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result removeBaseTrademark(@PathVariable("id") Long id){

        baseTrademarkService.removeById(id);
        return Result.ok();
    }


    /**
     * 按照id查询
     * @param id
     * @return
     */
    @GetMapping("/baseTrademark/get/{id}")
    public Result getbaseTrademarkById(@PathVariable("id") Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }


    /**
     * 保存品牌
     * @param baseTrademark
     * @return
     */
    @PostMapping("/baseTrademark/save")
    public Result saveTrademark(@RequestBody BaseTrademark baseTrademark){

        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 修改品牌
     * @param baseTrademark
     * @return
     */
    @PutMapping("/baseTrademark/update")
    public Result updateTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

}
