package com.example.mqtest.demos.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SpringRabbitListener
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/17 16:28
 * @Version: 1.0
 */

@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "hello")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
    }
}
