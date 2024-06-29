package com.example.mqtest.demos.service;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName: RabbitMQService
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 17:22
 * @Version: 1.0
 */

@Service
public class RabbitMQService {

        @Resource
        private RabbitTemplate rabbitTemplate;

        /**
         * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
         */
        @PostConstruct
        public void init(){
            /**
             * 消息投递到交换机后触发回调
             * 使用该功能需要开启确认，spring-boot中配置如下：
             * publisher-confirm-type: correlated   # 开启确认机制/老版 publisher-confirms: true
             */
            rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if(ack){
                        System.err.println("消息投递成功，消息已确认->"+cause+"\t"+correlationData);
                    }else{
                        /**
                         * 如果消息投递失败需要设置重发   如果一直重发失败投递到死信队列中/数据库 进行手动排查
                         * 重发数据库+定时任务实现
                         */
                        System.err.println("消息投递失败，消息未确认->"+cause+"\t"+correlationData);
                    }
                }
            });

            /**
             * 通过实现ReturnsCallback接口
             * 如果消息从交换机投递到队列中失败时触发
             * 比如根据发送消息指定Routingkey找不到队列时触发
             * 使用该功能需要开启确认，spring-boot中配置如下：
             * spring.rabbitmq.publisher-returns = true
             */


        }

        /*
        @PostConstruct
        public void  init2(){
            rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
                @Override
                public void returnedMessage(ReturnedMessage returned) {
                    //需要将接收失败的保存到DB中并且手动排错
                    System.err.println("队列接收消息失败，消息被退回"+returned);
                }
            });
        }

         */

}
