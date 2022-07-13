package com.atguigu.gmall.model.to.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeckillQueueMsg {
    private Long userId;
    private Long skuId;
    private String seckillCode; //秒杀码
}
