package com.atguigu.gmall.order;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTemplateTest {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    void testSend(){
        rabbitTemplate.convertAndSend("haha","heihei1","66666");
    }
}
