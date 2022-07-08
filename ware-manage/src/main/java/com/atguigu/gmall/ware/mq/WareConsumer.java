package com.atguigu.gmall.ware.mq;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.ware.bean.WareOrderTask;
import com.atguigu.gmall.ware.constant.MqConst;
import com.atguigu.gmall.ware.enums.TaskStatus;
import com.atguigu.gmall.ware.mapper.WareOrderTaskDetailMapper;
import com.atguigu.gmall.ware.mapper.WareOrderTaskMapper;
import com.atguigu.gmall.ware.mapper.WareSkuMapper;
import com.atguigu.gmall.ware.service.GwareService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @param
 * @return
 */
@Component
public class WareConsumer {

    @Autowired
    private WareOrderTaskMapper wareOrderTaskMapper;

    @Autowired
    private WareOrderTaskDetailMapper wareOrderTaskDetailMapper;

    @Autowired
    private WareSkuMapper wareSkuMapper;

    @Autowired
    private GwareService gwareService;

    /**
     * 支付成功扣减库存
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_WARE_STOCK, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_WARE_STOCK, type = ExchangeTypes.DIRECT, durable = "true"),
            key = {MqConst.ROUTING_WARE_STOCK}
    ))
    public void paySuccess(String orderTaskJson, Message message, Channel channel) throws IOException {
        WareOrderTask wareOrderTask = JSON.parseObject(orderTaskJson, WareOrderTask.class);
        wareOrderTask.setTaskStatus(TaskStatus.PAID.name());
        gwareService.saveWareOrderTask(wareOrderTask);
        // 检查是否拆单！
        //
        List<WareOrderTask> wareSubOrderTaskList = gwareService.checkOrderSplit(wareOrderTask);
        if (wareSubOrderTaskList != null && wareSubOrderTaskList.size() >= 2) {
            for (WareOrderTask orderTask : wareSubOrderTaskList) {
                gwareService.lockStock(orderTask);
            }
        } else {
            gwareService.lockStock(wareOrderTask);
        }
        //确认收到消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
