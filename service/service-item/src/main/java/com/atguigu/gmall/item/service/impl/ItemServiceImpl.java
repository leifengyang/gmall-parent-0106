package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.item.component.CacheService;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.vo.CategoryView;
import com.atguigu.gmall.model.vo.SkuDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    SkuFeignClient skuFeignClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    CacheService cacheService;


    /**
     * 事务：
     *   1、获取链接
     *   2、设置非自动提交
     *   3、准备“sql”、设置参数【service的目标方法】
     *   4、执行sql
     *   5、返回结果
     *   6、提交/回滚
     *   7、关闭链接
     *    @Transactional： 利用AOP
     *     1)、目标方法执行之前： 1、2
     *     2)、目标方法执行
     *     3)、目标方法执行之后-正常结束： 5，6提交
     *     4)、目标方法执行之后-异常结束： 5，6回滚
     *     5)、目标方法无论怎样结束：      7关闭链接
     *
     *     前置通知、后置通知、返回通知、异常通知
     *     try{
     *         //1、前置通知
     *         业务代码  service.Hello();
     *         //4、返回通知
     *     }catch(Exception e){
     *         //3、异常通知
     *     }finally{
     *         //2、后置通知
     *     }
     *
     *
     * 缓存：
     *   1、先查缓存
     *   2、缓存没有，先问布隆
     *   3、布隆说有，获取分布式锁
     *   4、【执行业务回源查询】
     *   5、查到的数据放缓存
     *   6、解锁
     * AOP编写一个切面来实现功能+自定义注解@Cache
     *
     *
     * @param skuId
     * @return
     */


    @Override
    public SkuDetailVo getItemDetail(Long skuId) {
        String cacheKey = RedisConst.SKU_INFO_CACHE_KEY_PREFIX + skuId;
        //1、先查缓存。
        SkuDetailVo data = cacheService.getData(cacheKey, SkuDetailVo.class);
        //2、缓存中是否存在
        if(data == null){
            log.info("sku:{} 详情- 缓存不命中，准备回源..正在检索布隆是否存在这个商品");
            RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER_NAME);
            if (!filter.contains(skuId)) { //布隆说没有就一定没有
                log.info("sku:{} 布隆觉得没有，请求无法穿透...");
                return null;
            }
            //2.1、 缓存中没有。准备回源。
            //2.2、 加分布式锁  lock:sku:info:49
            RLock lock = redissonClient.getLock(RedisConst.SKU_INFO_LOCK_PREFIX + skuId);
            //2.3、加锁
            boolean tryLock = lock.tryLock();
            //2.4、获得锁
            if(tryLock){
                //2.7、准备回源
                SkuDetailVo detail = getItemDetailFromRpc(skuId);
                //2.8、缓存一份
                cacheService.saveData(cacheKey,detail,RedisConst.SKU_INFO_CACHE_TIMEOUT,TimeUnit.MILLISECONDS);
                lock.unlock();
                return detail;
            }else {
                //2.5、没获得锁
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    //2.6、直接查缓存即可
                    return  cacheService.getData(cacheKey, SkuDetailVo.class);
                } catch (InterruptedException e) {

                }
            }
        }

        //3、缓存中有这个数据，直接返回
        return data;
    }

//    @Override
//    public SkuDetailVo getItemDetailWithRedisLock(Long skuId){
//        //1、先查缓存
//        String json = redisTemplate.opsForValue().get("sku:info:" + skuId);
//        //2、判断缓存
//        if(StringUtils.isEmpty(json)){
//            //分配一个唯一标识的令牌
//            String token = UUID.randomUUID().toString().replace("-", "");
//            //2.1 缓存中没有。准备回源。加上分布式锁。 setnx key value：当这个key不存在才设置
//            //记录时间 start: 0
//            Boolean absent = redisTemplate.opsForValue().setIfAbsent("lock:" + skuId, token,10,TimeUnit.SECONDS);//lock:49  lock:50
//            if(absent){ //无需关注if是否能进来
////                redisTemplate.expire("lock:" + skuId,10,TimeUnit.SECONDS);
//                //2.1.1 抢到锁
//                SkuDetailVo rpc = getItemDetailFromRpc(skuId);
//                //2.2 把数据放入缓存
//                redisTemplate.opsForValue().set("sku:info:" + skuId,JSONs.toStr(rpc),7, TimeUnit.DAYS);
//
//                //2.3 解锁. 脚本：Lua  "if(redis.get(lock:49).equals(token)) redis.del(lock:49)"
//                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//                //RedisScript<T> script,
//                //List<K> keys,  //脚本中的KEYS[1]从这个里面取
//                //Object... args //脚本中的ARGV[1]从这个可变参数取
//
//                //执行删锁脚本。  记录时间end  10
//                // (end-start) < 10s；锁的时间内执行删锁。
//                Long execute = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList("lock:" + skuId), token);
//                if(execute == 0){
//                    log.info("又差点删到别人的锁了：lockKey=[{}],lockValue=[{}]","lock:" + skuId,token);
//                }
////              if(cc < 10){
////                   redisTemplate.del;
////              }
//                //异步。伴随线程
//                //while(true){ TimeUnit.SECONDS.sleep(5); //续满期 }
//
//                //这个删锁+判断不是原子操作，可能导致删除别人的锁。
////                String redisToken = redisTemplate.opsForValue().get("lock:" + skuId);
////                if(token.equals(redisToken)){
////                    //这是我的锁
////                    redisTemplate.delete("lock:" + skuId);
////                } //不是你的锁就不要管
//
//                return rpc;
//            }
//           //2.3 没抢到锁。等待
//            try {
//                TimeUnit.MILLISECONDS.sleep(300);
//                //2.4 直接查缓存
//                json = redisTemplate.opsForValue().get("sku:info:" + skuId);
//                return JSONs.toObj(json,SkuDetailVo.class);
//            } catch (InterruptedException e) {
//            }
//
//        }
//
//        //3、缓存中有
//        SkuDetailVo obj = JSONs.toObj(json, SkuDetailVo.class);
//        return obj;
//    }


    //
    public SkuDetailVo getItemDetailFromRpc(Long skuId) {
        SkuDetailVo vo = new SkuDetailVo();
        //2、sku的info
        Result<SkuInfo> skuInfo = skuFeignClient.getSkuInfo(skuId);
        SkuInfo info = skuInfo.getData();
        vo.setSkuInfo(info);


        //1、sku所在的分类。
        Long category3Id = info.getCategory3Id();
        //按照三级分类id查出所在的完整分类信息
        Result<CategoryView> categoryView = skuFeignClient.getCategoryView(category3Id);
        vo.setCategoryView(categoryView.getData());


        //3、sku的价格
        vo.setPrice(info.getPrice());

        //4、sku的销售属性列表
        Long spuId = info.getSpuId();
        Result<List<SpuSaleAttr>> saleAttr = skuFeignClient.getSaleAttr(skuId, spuId);
        if (saleAttr.isOk()) {
            vo.setSpuSaleAttrList(saleAttr.getData());
        }


        //5、得到一个sku对应的spu的所有sku的组合关系
        Result<String> value = skuFeignClient.getSpudeAllSkuSaleAttrAndValue(spuId);
        vo.setValuesSkuJson(value.getData());

        return vo;
    }
}
