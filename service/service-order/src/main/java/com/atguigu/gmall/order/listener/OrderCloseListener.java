package com.atguigu.gmall.order.listener;


import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.to.mq.OrderCreateMsg;
import com.atguigu.gmall.order.service.OrderBizService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCloseListener {


    @Autowired
    OrderBizService orderBizService;


    //MQ能极大程度保证业务一致性
    //幂等性问题: 怎么解决？ 业务自己保证=(关单期望状态+影响行数日志)；
    @RabbitListener(queues = MqConst.QUEUE_ORDER_DEAD)
    public void closeListener(Message message, Channel channel) throws Exception{
        MessageProperties properties = message.getMessageProperties();
//        if (properties.getRedelivered()) {
//            //不执行
//        }
        try {
            byte[] body = message.getBody();
            OrderCreateMsg msg = JSONs.toObj(new String(body), OrderCreateMsg.class);
            //执行业务。关单。
            log.info("收到过期订单，正在准备关闭：{}",msg);
            orderBizService.closeOrder(msg.getOrderId(),msg.getUserId());
            channel.basicAck(properties.getDeliveryTag(),false);
        }catch (Exception e){
            log.error("MQ业务处理失败：{}",e);
            channel.basicNack(properties.getDeliveryTag(),false,true);
        }
    }


}
