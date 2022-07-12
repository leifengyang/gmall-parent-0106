package com.atguigu.gmall.order.listener;


import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.vo.ware.OrderDeduceStatusVo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 感知订单的库存扣减状态
 */
@Slf4j
@Service
public class OrderWareStockStateListener {

    @Autowired
    OrderInfoService orderInfoService;


    @RabbitListener(queues = MqConst.QUEUE_WARE_ORDER)
    public void orderStock(Message message, Channel channel) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        try {
            String json = new String(message.getBody());
            OrderDeduceStatusVo statusVo = JSONs.toObj(json, OrderDeduceStatusVo.class);

            /**
             * 状态： ‘DEDUCTED’  (已减库存)
             * 状态：  ‘OUT_OF_STOCK’  (库存超卖)
             */
            String status = statusVo.getStatus(); //
            ProcessStatus changeStatus = null;
            switch (status){
                case "DEDUCTED":   changeStatus = ProcessStatus.WAITING_DELEVER ;break;
                case "OUT_OF_STOCK": changeStatus = ProcessStatus.STOCK_EXCEPTION; break;
            }
            //更新订单的状态
            orderInfoService.updateOrderStatus(
                    statusVo.getOrderId(),
                    statusVo.getUserId(),
                    changeStatus.getOrderStatus().name(),
                    changeStatus.name(),
                    ProcessStatus.PAID.name()
                    );
            channel.basicAck(properties.getDeliveryTag(),false);
        }catch (Exception e){
            log.error("异常：{}",e);
            channel.basicNack(properties.getDeliveryTag(),false,true);
        }
    }
}
