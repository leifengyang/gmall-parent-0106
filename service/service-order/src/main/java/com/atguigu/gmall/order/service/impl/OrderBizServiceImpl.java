package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderStatusLog;
import com.atguigu.gmall.model.to.mq.OrderCreateMsg;
import com.atguigu.gmall.model.vo.order.CartOrderDetailVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.service.OrderStatusLogService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class OrderBizServiceImpl implements OrderBizService {


    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    SkuFeignClient skuFeignClient;

    @Autowired
    WareFeignClient wareFeignClient;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderStatusLogService orderStatusLogService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(16);


    @Autowired
    OrderBizService orderBizService;

    @Override
    public OrderConfirmVo getOrderConfirmData() {
        OrderConfirmVo confirmVo = new OrderConfirmVo();

        //注意：用户需要透传id
        RequestAttributes or = RequestContextHolder.getRequestAttributes();


        CompletableFuture<Void> userAddressAsyncFuture = CompletableFuture.runAsync(() -> {
            //1、获取用户收货地址列表
            RequestContextHolder.setRequestAttributes(or);
            confirmVo.setUserAddressList(userFeignClient.getUserAddress().getData());
            RequestContextHolder.resetRequestAttributes();
        }, executor);


        //2、获取购物车中选中商品
        CompletableFuture<List<CartInfo>> checkedCartItemsAsync = CompletableFuture
                .supplyAsync(() -> {
                    RequestContextHolder.setRequestAttributes(or);
                    List<CartInfo> data = cartFeignClient.getCheckedCartItems().getData();
                    RequestContextHolder.resetRequestAttributes();
                    return data;
                }, executor);

        //3、处理每个商品价格、库存等信息
        CompletableFuture<List<CartOrderDetailVo>> detailVosAsync = checkedCartItemsAsync.thenApplyAsync(checkedItems -> {
            List<CartOrderDetailVo> detailVos = checkedItems.stream()
                    .parallel()
                    .map(cartInfo -> {
                        CartOrderDetailVo detailVo = new CartOrderDetailVo();
                        //查询最新价格
                        Result<BigDecimal> price = skuFeignClient.get1010SkuPrice(cartInfo.getSkuId());
                        detailVo.setOrderPrice(price.getData());
                        detailVo.setImgUrl(cartInfo.getImgUrl());
                        detailVo.setSkuName(cartInfo.getSkuName());
                        detailVo.setSkuNum(cartInfo.getSkuNum());
                        detailVo.setSkuId(cartInfo.getSkuId());
                        //远程调用库存系统;
                        String stock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
                        detailVo.setStock(stock);
                        return detailVo;
                    }).collect(Collectors.toList());
            //2、获取购物车中选中的需要结算的商品
            confirmVo.setDetailArrayList(detailVos);
            return detailVos;
        }, executor);


        //3、总数量
        CompletableFuture<Void> totalNumAsync = checkedCartItemsAsync.thenAcceptAsync(items -> {
            Integer totalNum = items.stream()
                    .map(cartInfo -> cartInfo.getSkuNum())
                    .reduce((o1, o2) -> o1 + o2)
                    .get();
            confirmVo.setTotalNum(totalNum);
        }, executor);


        //4、总金额  每个商品实时价格*数量 的加和
        CompletableFuture<Void> totalAmountAsync = detailVosAsync.thenAcceptAsync(details -> {
            BigDecimal totalAmount = details.stream()
                    .map(cart -> cart.getOrderPrice().multiply(new BigDecimal(cart.getSkuNum())))
                    .reduce((o1, o2) -> o1.add(o2))
                    .get();
            confirmVo.setTotalAmount(totalAmount);
        }, executor);


        //5、防重令牌，追踪号
        confirmVo.setTradeNo(generateTradeToken());


        //等所有结果做完
        CompletableFuture.allOf(userAddressAsyncFuture, detailVosAsync, totalNumAsync, totalAmountAsync)
                .join();


        return confirmVo;
    }

    @Override
    public String generateTradeToken() {
        //1、生成令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //2、给redis一份;  trade:token:uuid； 10分钟过期，页面10分钟不动就必须重新刷新
        redisTemplate.opsForValue().set(RedisConst.TRADE_TOKEN_PREFIX + token,RedisConst.A_KEN_VALUE,10, TimeUnit.MINUTES);
        return token;
    }


    //原子验令牌
    @Override
    public boolean checkTradeToken(String token) {
        String scrpit =  "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(scrpit, Long.class),
                Arrays.asList(RedisConst.TRADE_TOKEN_PREFIX + token),
                RedisConst.A_KEN_VALUE);
        return execute == 1L; //true
    }

    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo order) {
        //1、验令牌
        boolean token = checkTradeToken(tradeNo);
        if(!token){
            throw new GmallException(ResultCodeEnum.ORDER_INVAILD_TOKEN);
        }

        //2、验库存
        List<String> noStockSku = order.getOrderDetailList().stream()
                .filter(item -> {
                    Long skuId = item.getSkuId();
                    Integer skuNum = item.getSkuNum();
                    String stock = wareFeignClient.hasStock(skuId, skuNum);
                    return "0".equals(stock);
                }).map(item -> item.getSkuName())
                .collect(Collectors.toList());

        if(noStockSku!=null && noStockSku.size()>0){
            GmallException exception = new GmallException(
                    ResultCodeEnum.ORDER_ITEM_NO_STOCK.getMessage()+ JSONs.toStr(noStockSku),
                    ResultCodeEnum.ORDER_ITEM_NO_STOCK.getCode()
                    );
            throw exception;
        }

        //3、验价格
        List<String> priceChangeSku = order.getOrderDetailList().stream()
                .filter(item -> {
                    Result<BigDecimal> price = skuFeignClient.get1010SkuPrice(item.getSkuId());
                    return !item.getOrderPrice().equals(price.getData());
                }).map(item -> item.getSkuName())
                .collect(Collectors.toList());
        if(priceChangeSku!=null && priceChangeSku.size()>0){
            GmallException exception = new GmallException(
                    ResultCodeEnum.ORDER_ITEM_PRICE_CHANGE.getMessage()+ JSONs.toStr(priceChangeSku),
                    ResultCodeEnum.ORDER_ITEM_PRICE_CHANGE.getCode()
            );
            throw exception;
        }


        //1)、代理对象.事务方法();
        //2)、代理对象.事务方法(); 由于其他切面的提前介入吃掉异常就完蛋了。
        //3)、异步没事务。  TransactionContextHolder； 线上永远不异步做事务。ACID；
        // 分布式事务、
        // 1、Seata AT分布式强一致事务。（不用与高并发）
        // 2、【柔性事务】MQ最终一致性。 Seata T(try)C(confirm)C(cancle)[]；
//        OrderBizService proxy = AopContext.currentProxy();
//        Function<Context, Context> holder = TransactionContextManager.getOrCreateContextHolder();


        //4、保存订单： 事务的方法一定要动态代理调用; 事务失效的1x大场景
        OrderInfo info =  orderBizService.saveOrder(tradeNo, order);

        //5、删除购物车中这个商品。
        cartFeignClient.deleteChecked();

        //用户服务加积分。（支付成功做）

        //7、订单 30min 以后未支付就关闭订单(订单状态改为close)。支付扣库存。返库存：（取消、退）
        //1）、异步
//        CompletableFuture.runAsync(()->{
//            Thread.sleep(30 );
//            orderBizService.close(); //1、判断是否支付。2、没支付就关闭，3、支付了就不管。
//        });
//
//        //2）、异步延迟任务; 如果机器宕机，任务丢失。 (分布式延迟任务)+【xxl-job（Elastic-Job）】
//        threadPool.schedule(()->{
//            orderBizService.close();  //1、判断是否支付。2、没支付就关闭，3、支付了就不管。
//        },30,TimeUnit.MINUTES);

        //解决 异步延迟任务丢失问题：使用分布式任务框架 【xxl-job、Elastic-Job】


        //5、发送消息给MQ； //orderId、userId、totalAmout、status
        OrderCreateMsg msg = prepareOrderMsg(info);
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_ORDER_EVENT,MqConst.RK_ORDER_CREATE,JSONs.toStr(msg));
        return info.getId(); //"返回订单id";
    }

    private OrderCreateMsg prepareOrderMsg(OrderInfo info) {
        return  new OrderCreateMsg(info.getId(),info.getUserId(),info.getTotalAmount(),info.getOrderStatus());
    }

//    @Scheduled(cron = "* */30 * * * ?")
//    public void closeOrder(){
//
//    }

    @Transactional //事务在异步情况下用不了
    @Override
    public OrderInfo saveOrder(String tradeNo,OrderSubmitVo order) {
        //1、订单信息保存
        OrderInfo orderInfo = prepareOrderInfo(tradeNo,order);
        orderInfoService.save(orderInfo);

        //2、订单明细保存
        orderInfoService.saveDetail(orderInfo,order); //新：外面不影响里面，但是里面会影响外面


        //3、订单日志记录
        OrderStatusLog log = prepareOrderStatusLog(orderInfo);
        orderStatusLogService.save(log);

        return orderInfo;
    }

    @Transactional
    @Override
    public void closeOrder(Long orderId, Long userId) {
        ProcessStatus closeStatus = ProcessStatus.CLOSED;

        //1、修改订单状态为已关闭
        orderInfoService.updateOrderStatus(orderId,userId,
                closeStatus.getOrderStatus().name(),closeStatus.name(),ProcessStatus.UNPAID.name());


    }

    private OrderStatusLog prepareOrderStatusLog(OrderInfo orderInfo) {
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderInfo.getId());
        log.setUserId(userId);
        log.setOrderStatus(orderInfo.getOrderStatus());
        log.setOperateTime(new Date());
        return log;
    }

    //准备订单数据
    private OrderInfo prepareOrderInfo(String tradeNo,OrderSubmitVo vo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setConsignee(vo.getConsignee());
        orderInfo.setConsigneeTel(vo.getConsigneeTel());


        BigDecimal total = vo.getOrderDetailList().stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())))
                .reduce((o1, o2) -> o1.add(o2))
                .get();
        orderInfo.setTotalAmount(total); //订单结账总价

        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        Long userId = AuthContextHolder.getUserAuth().getUserId();
        orderInfo.setUserId(userId);
        //线上支付
        orderInfo.setPaymentWay("ONLINE");

        //收货人地址
        orderInfo.setDeliveryAddress(vo.getDeliveryAddress());

        //订单备注
        orderInfo.setOrderComment(vo.getOrderComment());

        //对外交易号；流水号    8+32+10  8byte*8bit;  1000000000
        orderInfo.setOutTradeNo("ATGUIGU_"+tradeNo+"_"+userId);


        //交易body：购买了那些商品名字
        String allName = vo.getOrderDetailList().stream()
                .map(item -> item.getSkuName())
                .reduce((o1, o2) -> o1 + "<br/>" + o2)
                .get();
        orderInfo.setTradeBody(allName);


        //创建时间
        orderInfo.setCreateTime(new Date());

        //过期时间； 订单30min；不支付，就会被取消。
        Long m = new Date().getTime()+1000*60*30L;
        Date date = new Date(m);
        orderInfo.setExpireTime(date);


        //订单的处理状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());


        //物流号，发货以后才会有
        orderInfo.setTrackingNo("");

        //订单需要拆单的话，要指定父单id
        orderInfo.setParentOrderId(0L);


        //第一个商品的图片，未来展示订单列表的时候，可以看的
        orderInfo.setImgUrl(vo.getOrderDetailList().get(0).getImgUrl());

        //仓库id。
        orderInfo.setWareId("");


        orderInfo.setProvinceId(0L);

        //远程调用
        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));

        //订单的原始总价;  TotalAmount = OriginalTotalAmount - ActivityReduceAmount - CouponAmount
        orderInfo.setOriginalTotalAmount(total);

        //收到货以后，立即修改为 30天后
        orderInfo.setRefundableTime(new Date());//

        //运费系统，RPC计算出运费；第三方物流系统，
        // 快递100；
        //菜鸟驿站： https://tech.cainiao.com/productv2/ProductIndex/22
        orderInfo.setFeightFee(new BigDecimal("0"));

        //操作时间
        orderInfo.setOperateTime(new Date());



        return orderInfo;
    }
}
