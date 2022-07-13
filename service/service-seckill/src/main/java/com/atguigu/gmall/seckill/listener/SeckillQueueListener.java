package com.atguigu.gmall.seckill.listener;


import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.to.mq.SeckillQueueMsg;
import com.atguigu.gmall.seckill.service.SeckillBizService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SeckillQueueListener {

    @Autowired
    SeckillBizService seckillBizService;

    @RabbitListener(queues = MqConst.QUEUE_SECKILL_QUEUE)
    public void seckillQueue(Message message, Channel channel) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        try {
            //1、得到秒杀排队消息
            SeckillQueueMsg msg = JSONs.toObj(new String(message.getBody()), SeckillQueueMsg.class);
            //2、进行秒杀下单； redis的数量和数据库的一起扣。
            //redis不扣也行。最终以数据库为准。
            seckillBizService.generateSeckillOrder(msg);

        }catch (Exception e){
            log.error("秒杀处理失败：");
        }finally {
            channel.basicAck(properties.getDeliveryTag(),false);
        }
    }
}
