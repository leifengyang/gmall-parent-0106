package com.atguigu.gmall.ware.config;


import com.atguigu.gmall.ware.constant.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {


    @Bean
    public Exchange queueWareOrderExchange(){
        return new TopicExchange(MqConst.EXCHANGE_DIRECT_WARE_ORDER,true,false);
    }
    /**
     * 库存扣减结果队列
     * @return
     */
    @Bean
    public Queue queueWareOrder(){
       return new Queue(MqConst.QUEUE_WARE_ORDER,true,false,false);
    }

    /**
     * 库存扣减结果队列 绑定
     * @return
     */
    @Bean
    public Binding queueWareOrderBinding(){
        return new Binding(
                MqConst.QUEUE_WARE_ORDER,
                Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_DIRECT_WARE_ORDER,
                MqConst.ROUTING_WARE_ORDER,
                null);
    }
}
