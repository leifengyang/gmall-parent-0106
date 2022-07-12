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
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderStatusLog;
import com.atguigu.gmall.model.to.mq.OrderCreateMsg;
import com.atguigu.gmall.model.to.mq.WareStockDetail;
import com.atguigu.gmall.model.vo.order.CartOrderDetailVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.ware.OrderSpiltVo;
import com.atguigu.gmall.model.vo.ware.OrderSplitRespVo;
import com.atguigu.gmall.model.vo.ware.WareFenBuVo;
import com.atguigu.gmall.order.service.OrderBizService;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.service.OrderStatusLogService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    OrderDetailService orderDetailService;

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
        redisTemplate.opsForValue().set(RedisConst.TRADE_TOKEN_PREFIX + token, RedisConst.A_KEN_VALUE, 10, TimeUnit.MINUTES);
        return token;
    }


    //原子验令牌
    @Override
    public boolean checkTradeToken(String token) {
        String scrpit = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(scrpit, Long.class),
                Arrays.asList(RedisConst.TRADE_TOKEN_PREFIX + token),
                RedisConst.A_KEN_VALUE);
        return execute == 1L; //true
    }

    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo order) {
        //1、验令牌
        boolean token = checkTradeToken(tradeNo);
        if (!token) {
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

        if (noStockSku != null && noStockSku.size() > 0) {
            GmallException exception = new GmallException(
                    ResultCodeEnum.ORDER_ITEM_NO_STOCK.getMessage() + JSONs.toStr(noStockSku),
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
        if (priceChangeSku != null && priceChangeSku.size() > 0) {
            GmallException exception = new GmallException(
                    ResultCodeEnum.ORDER_ITEM_PRICE_CHANGE.getMessage() + JSONs.toStr(priceChangeSku),
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
        OrderInfo info = orderBizService.saveOrder(tradeNo, order);

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
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_ORDER_EVENT, MqConst.RK_ORDER_CREATE, JSONs.toStr(msg));
        return info.getId(); //"返回订单id";
    }

    private OrderCreateMsg prepareOrderMsg(OrderInfo info) {
        return new OrderCreateMsg(info.getId(), info.getUserId(), info.getTotalAmount(), info.getOrderStatus());
    }

//    @Scheduled(cron = "* */30 * * * ?")
//    public void closeOrder(){
//
//    }

    @Transactional //事务在异步情况下用不了
    @Override
    public OrderInfo saveOrder(String tradeNo, OrderSubmitVo order) {
        //1、订单信息保存
        OrderInfo orderInfo = prepareOrderInfo(tradeNo, order);
        orderInfoService.save(orderInfo);

        //2、订单明细保存
        orderInfoService.saveDetail(orderInfo, order); //新：外面不影响里面，但是里面会影响外面


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
        orderInfoService.updateOrderStatus(orderId, userId,
                closeStatus.getOrderStatus().name(), closeStatus.name(), ProcessStatus.UNPAID.name());


    }

    @Transactional
    @Override
    public List<OrderSplitRespVo> splitOrder(OrderSpiltVo vo) {
        List<OrderSplitRespVo> result = new ArrayList<>();
        //1、查询当前父订单以及详情
        OrderInfo orderInfo = orderInfoService
                .getOrderInfoAndDetails(Long.parseLong(vo.getOrderId()), Long.parseLong(vo.getUserId()));

        //2、按照库存分布拆分出新单
        String skuMapJson = vo.getWareSkuMap();
        //3、得到库存分布信息
        List<WareFenBuVo> fenBuVos = JSONs.toObj(skuMapJson, new TypeReference<List<WareFenBuVo>>() {
        });

        //4、按照仓库拆单
        for (WareFenBuVo buVo : fenBuVos) {
            //保存子订单
            OrderSplitRespVo order = saveChildOrder(orderInfo, buVo);
            result.add(order);
        }

        //5、父单改为已拆分状态
        ProcessStatus split = ProcessStatus.SPLIT;
        //也会推进日志
        orderInfoService.updateOrderStatus(orderInfo.getId(),
                orderInfo.getUserId(),
                split.getOrderStatus().name(),
                split.name(),
                ProcessStatus.PAID.name());


        return result;
    }

    @Transactional
    @Override
    public OrderSplitRespVo saveChildOrder(OrderInfo orderInfo, WareFenBuVo buVo) {
        //1、准备子订单
        OrderInfo childOrder = prepareChildOrder(orderInfo,buVo);

        //2、保存子订单 order_info
        orderInfoService.save(childOrder);

        //3、保存子订单详情
        List<OrderDetail> detailList =
        childOrder.getOrderDetailList().stream().map(item->{
            item.setOrderId(childOrder.getId()); //设置号子单id即可
            return item;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(detailList);


        //4、准备返回
        return prepareOrderSplitRespVo(childOrder);

    }

    private OrderSplitRespVo prepareOrderSplitRespVo(OrderInfo childOrder) {
        OrderSplitRespVo respVo = new OrderSplitRespVo();
        respVo.setOrderId(childOrder.getId()+"");
        respVo.setConsignee(childOrder.getConsignee());
        respVo.setConsigneeTel(childOrder.getConsigneeTel());
        respVo.setOrderComment(childOrder.getOrderComment());
        respVo.setOrderBody(childOrder.getTradeBody());
        respVo.setDeliveryAddress(childOrder.getDeliveryAddress());
        respVo.setPaymentWay("2");
        respVo.setWareId(childOrder.getWareId());

        //子单负责的所有商品详情
        //List<WareStockDetail>
        List<WareStockDetail> details = childOrder.getOrderDetailList().stream()
                .map(item -> new WareStockDetail(item.getSkuId(), item.getSkuNum(), item.getSkuName()))
                .collect(Collectors.toList());
        respVo.setDetails(details);

        return respVo;
    }

    private OrderInfo prepareChildOrder(OrderInfo orderInfo, WareFenBuVo buVo) {
        OrderInfo childOrder = new OrderInfo();

        //设置订单详情；
        Set<String> skuIds = buVo.getSkuIds().stream().collect(Collectors.toSet());
        //获取总单中子订单负责的商品
        List<OrderDetail> orderDetails = orderInfo.getOrderDetailList().stream()
                .filter(item -> skuIds.contains(item.getSkuId().toString()))
                .collect(Collectors.toList());
        //得到当前子订单负责的商品
        childOrder.setOrderDetailList(orderDetails);

        childOrder.setConsignee(orderInfo.getConsignee());
        childOrder.setConsigneeTel(orderInfo.getConsigneeTel());

        //拆单后包含的商品的总额
        BigDecimal total = orderDetails.stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())))
                .reduce((o1, o2) -> o1.add(o2))
                .get();
        childOrder.setTotalAmount(total);


        //每个子单自己购买的商品的名字
        childOrder.setTradeBody(orderDetails.get(0).getSkuName());
        //每个子单自己购买的商品的图片
        childOrder.setImgUrl(orderDetails.get(0).getImgUrl());
        //子单拆分的时间
        childOrder.setCreateTime(new Date());

        //子单涉及到的商品原始总额
        childOrder.setOriginalTotalAmount(new BigDecimal("0"));
        //子单每个商品自己的优惠额
        childOrder.setActivityReduceAmount(new BigDecimal("0"));
        childOrder.setCouponAmount(new BigDecimal("0"));

        //每个子单最终配送以后会有自己的物流号
        childOrder.setTrackingNo("");

        childOrder.setOperateTime(new Date());

        childOrder.setOrderStatus(orderInfo.getOrderStatus());
        childOrder.setUserId(orderInfo.getUserId());
        childOrder.setPaymentWay(orderInfo.getPaymentWay());
        childOrder.setDeliveryAddress(orderInfo.getDeliveryAddress());
        childOrder.setOrderComment(orderInfo.getOrderComment());
        childOrder.setOutTradeNo(orderInfo.getOutTradeNo());
        childOrder.setExpireTime(orderInfo.getExpireTime());
        childOrder.setProcessStatus(orderInfo.getProcessStatus());
        childOrder.setParentOrderId(orderInfo.getParentOrderId());
        childOrder.setWareId(buVo.getWareId());
        childOrder.setRefundableTime(orderInfo.getRefundableTime());
        childOrder.setFeightFee(new BigDecimal("0"));



        return childOrder;
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
    private OrderInfo prepareOrderInfo(String tradeNo, OrderSubmitVo vo) {
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
        orderInfo.setOutTradeNo("ATGUIGU_" + tradeNo + "_" + userId);


        //交易body：购买了那些商品名字
        String allName = vo.getOrderDetailList().stream()
                .map(item -> item.getSkuName())
                .reduce((o1, o2) -> o1 + "<br/>" + o2)
                .get();
        orderInfo.setTradeBody(allName);


        //创建时间
        orderInfo.setCreateTime(new Date());

        //过期时间； 订单30min；不支付，就会被取消。
        Long m = new Date().getTime() + 1000 * 60 * 30L;
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
