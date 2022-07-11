package com.atguigu.gmall.common.constant;

public class MqConst {

    // 微服务名-event-exchange
    public static final String EXCHANGE_ORDER_EVENT = "order-event-exchange";


    //订单关单延迟队列   微服务名-功能-queue
    public static final String QUEUE_ORDER_DELAY = "order-delay-queue";


    //订单超时路由键    微服务名-事件
    public static final String RK_ORDER_TIMEOUT = "order.timeout";

    //订单超时
    public static final long ORDER_TIMEOUT = 60000*30;

    public static final String RK_ORDER_CREATE = "order.create";

    public static final String QUEUE_ORDER_DEAD = "order-dead-queue";
}
