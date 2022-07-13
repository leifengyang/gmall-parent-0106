package com.atguigu.gmall.seckill.service.impl;

import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.to.mq.SeckillQueueMsg;
import com.atguigu.gmall.seckill.service.SeckillBizService;
import com.atguigu.gmall.seckill.service.SeckillGoodsCacheService;
import com.atguigu.gmall.seckill.service.SeckillGoodsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillBizServiceImpl implements SeckillBizService {


    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    SeckillBizService seckillBizService;

    @Autowired
    SeckillGoodsCacheService seckillGoodsCacheService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //本地缓存
    Map<String, Map<Long, SeckillGoods>> localCache = new HashMap<>();


    @Override
    public void uploadSeckillGoods(String day) {
        //1、先去数据库查到指定这天参与秒杀的所有商品
        List<SeckillGoods> goods = seckillGoodsService.getDaySeckillGoodsFromDb(day);

        //2、预热到缓存
        seckillGoodsCacheService.saveToCache(day, goods);
    }


    /**
     * 这个方法会被经常攻击；
     *
     * @param skuId
     * @return
     */
    @Override
    public String generateSeckillCode(Long skuId) {
        //从多级缓存中得到当前商品的信息
        SeckillGoods good = seckillGoodsCacheService.getSeckillGood(skuId);
        //1、是否到了时间
        Date date = new Date();
        if (!date.after(good.getStartTime())) {  //秒杀还没开始
            throw new GmallException(ResultCodeEnum.SECKILL_NO_START);
        }

        if (!date.before(good.getEndTime())) {  //秒杀结束
            throw new GmallException(ResultCodeEnum.SECKILL_END);
        }

        //2、大概校验库存。只需要判断本地。 //本地说有库存，不一定有，本地说没有一定没有；
        if (good.getStockCount() <= 0) {
            //本地没库存
            throw new GmallException(ResultCodeEnum.SECKILL_FINISH);
        }


        //3、生成秒杀码；【保证同一个用户，在同一天，同一个商品只能参与一次秒杀活动】
        // MD5(2022-07-13 + userId + skuId)
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        //得到一个秒杀码；  算法校验+远程redis校验
        String seckillCode = MD5.encrypt(DateUtil.formatDate(new Date()) + userId + "" + skuId);
        //redis再存一份  seckill:code:生成的码 = 0
        //码生成以后会重置为0
        redisTemplate.opsForValue().setIfAbsent(RedisConst.SECKILL_CODE_PREFIX + seckillCode, "0", 1, TimeUnit.DAYS);


        return seckillCode;
    }


    /**
     * 秒杀扣库存（高并发扣库存）
     * 怎么才代表秒杀下单成功？请求合法并且有库存
     *
     * @param skuId
     * @param skuIdStr
     */
    @Override
    public void seckillOrderSubmit(Long skuId, String skuIdStr) {
        //请求是否合法（秒杀时间、秒杀码）
        SeckillGoods good = seckillGoodsCacheService.getSeckillGood(skuId);

        //1、是否到了时间
        Date date = new Date();
        if (!date.after(good.getStartTime())) {  //秒杀还没开始
            throw new GmallException(ResultCodeEnum.SECKILL_NO_START);
        }

        if (!date.before(good.getEndTime())) {  //秒杀结束
            throw new GmallException(ResultCodeEnum.SECKILL_END);
        }

        //2、校验秒杀码
        if (!redisTemplate.hasKey(RedisConst.SECKILL_CODE_PREFIX + skuIdStr)) {
            throw new GmallException(ResultCodeEnum.SECKILL_ILLEGAL);
        }


        //3、判断是否多次重复下单。进行原子自增

        Long increment = redisTemplate.opsForValue().increment(RedisConst.SECKILL_CODE_PREFIX + skuIdStr);
        if (increment > 1) {
            //代表请求已经发过了。单下过了
            throw new GmallException(ResultCodeEnum.SUCCESS);
        }


        //4、大概判断是否有库存
        if (good.getStockCount() <= 0) {
            throw new GmallException(ResultCodeEnum.SECKILL_FINISH);
        }
        good.setStockCount(good.getStockCount() - 1);

        //5、秒杀异步下单。直接发个消息.
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        SeckillQueueMsg queueMsg = new SeckillQueueMsg(userId, skuId, skuIdStr);
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_SECKILL_EVENT, MqConst.RK_SECKILL_QUEUE, JSONs.toStr(queueMsg));


    }

    @Transactional
    @Override
    public void generateSeckillOrder(SeckillQueueMsg msg) {
        Long skuId = msg.getSkuId();
        //1、扣除数据库的库存；
        long l = seckillGoodsService.deduceSeckillStockCount(skuId, 1);
        if (l > 0) {
            //2、数据库扣库存成功。接下来就要为这个秒杀创建订单
            //后续流程比较复杂。比较慢，都可以发消息
            //生成一个临时订单，保存到缓存中。
            //3、扣redis
            String date = DateUtil.formatDate(new Date());

            //4、拿到redis中这个数据
            String jsonStr = (String) redisTemplate.opsForHash()
                    .get(RedisConst.SECKILL_GOODS_CACHE_PREFIX + date, skuId.toString());
            SeckillGoods goods = JSONs.toObj(jsonStr, SeckillGoods.class);
            goods.setStockCount(goods.getStockCount()-1);
            //5、重新保存redis
            redisTemplate.opsForHash().put(RedisConst.SECKILL_GOODS_CACHE_PREFIX + date,skuId.toString(),JSONs.toStr(goods));

            //6、发个消息创建订单。 给redis中最终保存一个 seckill:order:秒杀码 = 订单json临时数据
            //seckill:order:秒杀码 == 订单信息/错误信息
            saveTempSeckillOrder(msg,0); //0代表正常
        }else {
            saveTempSeckillOrder(msg,1);
        }

    }

    @Override
    public ResultCodeEnum checkOrderStatus(Long skuId) {
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        String seckillCode = MD5.encrypt(DateUtil.formatDate(new Date()) + userId + "" + skuId);
        //1、先判断redis是否有这个临时单
        String key = RedisConst.SECKILL_ORDER_PREFIX + seckillCode;
        String json = redisTemplate.opsForValue().get(key);
        //1）、redis纯粹没有 key； 早期校验都没有库存，直接没发消息。
        if(StringUtils.isEmpty(json)){
            //没有。 正在排队。库存失败呢？
            String incre = redisTemplate.opsForValue().get(RedisConst.SECKILL_CODE_PREFIX + seckillCode);
            if(Integer.parseInt(incre)>0){
                //用户发过请求。
                return ResultCodeEnum.SECKILL_RUN;
            }
        }else {
            if("error".equals(json)){
                //已售完
                return ResultCodeEnum.SECKILL_FAIL;
            }
            //有。判断redis中的临时单是否有id，如果有，代表以前下单结束了，订单服务都保存了订单，同步到redis中
            OrderInfo info = JSONs.toObj(json, OrderInfo.class);
            if(info.getId() == null){
                return ResultCodeEnum.SECKILL_SUCCESS;
            }else {
                return ResultCodeEnum.SECKILL_ORDER_SUCCESS;
            }
        }
        return ResultCodeEnum.SECKILL_FAIL;
    }


    private void saveTempSeckillOrder(SeckillQueueMsg msg,int type) {
        String code = msg.getSeckillCode();
        String key = RedisConst.SECKILL_ORDER_PREFIX +code;

        if(type==1){
            //库存扣失败
            redisTemplate.opsForValue().set(key,"error",1,TimeUnit.DAYS);
        }else if (type == 0){

         //seckill:order:你的秒杀码 = 秒杀单/错误信息

            SeckillGoods good = seckillGoodsCacheService.getSeckillGood(msg.getSkuId());

            //1、准备临时订单数据，先保存redis
            OrderInfo orderInfo = prepareTempOrder(msg, good);
            redisTemplate.opsForValue().set(key,JSONs.toStr(orderInfo),1,TimeUnit.DAYS);
        }
    }


    private OrderInfo prepareTempOrder(SeckillQueueMsg msg, SeckillGoods good) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setTotalAmount(good.getCostPrice()); //订单总额就是秒杀的商品秒杀价
        orderInfo.setOrderStatus(ProcessStatus.UNPAID.getOrderStatus().name());
        orderInfo.setUserId(msg.getUserId());

        orderInfo.setPaymentWay("2");

        orderInfo.setOutTradeNo("ATGUIGU-"+ UUID.randomUUID().toString().replace("-",""));
        orderInfo.setTradeBody(good.getSkuName());
        orderInfo.setCreateTime(new Date());


        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        orderInfo.setImgUrl(good.getSkuDefaultImg());

        //这个订单买了哪些商品
        List<OrderDetail> details = prepareTempOrderDetail(good,msg);
        orderInfo.setOrderDetailList(details);


        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        //设置优惠价
        orderInfo.setCouponAmount(good.getPrice().subtract(good.getCostPrice()));
        orderInfo.setOriginalTotalAmount(good.getPrice());

        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());
        return orderInfo;
    }

    private List<OrderDetail> prepareTempOrderDetail(SeckillGoods good,SeckillQueueMsg msg) {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setSkuId(good.getSkuId());
        orderDetail.setSkuName(good.getSkuName());
        orderDetail.setImgUrl(good.getSkuDefaultImg());
        orderDetail.setOrderPrice(good.getCostPrice());
        orderDetail.setSkuNum(1);
        orderDetail.setUserId(msg.getUserId());
        orderDetail.setHasStock("1");
        orderDetail.setCreateTime(new Date());
        orderDetail.setSplitTotalAmount(good.getCostPrice());



        return Arrays.asList(orderDetail);
    }

}
