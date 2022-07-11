package com.atguigu.gmall.order.mq;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HeheQueueListener {

    @RabbitListener(queues = "hehe")
    public void listener(Message message, Channel channel) throws Exception{
        MessageProperties properties = message.getMessageProperties();
        try {
            //TODO 执行你的业务

            byte[] body = message.getBody();
            System.out.println("收到消息："+new String(body));

            // long deliveryTag,
            // boolean multiple
            channel.basicAck(properties.getDeliveryTag(),false);
        }catch (Exception e){
            //业务失败；
            channel.basicNack(properties.getDeliveryTag(),false,true);
        }


    }
}
