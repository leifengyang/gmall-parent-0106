package com.atguigu.gmall.pay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MqConfiguration {

    //只需要把队列、交换机、绑定关系等放在容器中。MQ中没有就会自动创建

    @Bean
    RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);


        /**
         * message – the returned message.
         * replyCode – the reply code.
         * replyText – the reply text.
         * exchange – the exchange.
         * routingKey – the routing key.
         * 消息如果发出去了，就能调这个回调
         */
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            //消息发生错误调用【服务器没收到的】。持久化不成功；1、路由键错误，消息没给队列  2、队列的磁盘挂了。
            log.info("ReturnCallback: message:[{}]",message);
            //日志记录；（消息持久化失败； 1、路由键错误，消息没给队列  2、队列的磁盘挂了。）
            //数据库专门建立一张消息表，失败消息存到数据库，写一个定时任务，重试发送。
        });

        //mq确认消息了。会调用这个回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            //消息发生错误调用【服务器没回复（消息不能正确投递给队列）】
            log.info("ConfirmCallback: ack:[{}]， 原因：{}",ack,cause);
            //日志，ack：false不用记录；  ack: true；服务器确认收到了也有这个交换机，也有这个路由键。
        });

        return template;
    }







}
