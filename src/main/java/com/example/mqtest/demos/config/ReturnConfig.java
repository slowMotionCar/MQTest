package com.example.mqtest.demos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ReturnConfig
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 16:59
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class ReturnConfig  implements ApplicationContextAware {

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            // 获取RabbitTemplate
            RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
            // 设置ReturnCallback
            //这个lambda表达式具体的作用是定义了一个函数，这个函数接受五个参数：message、replyCode、replyText、exchange和routingKey，然后打印一条日志信息
            rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
                // 投递失败，记录日志
                //适用于消息传入了exchanger但是由于原因, 没传入queue
                log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                        replyCode, replyText, exchange, routingKey,
                        message.toString());
            });
        }
}
