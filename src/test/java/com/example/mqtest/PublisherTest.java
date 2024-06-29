package com.example.mqtest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: PublisherTest
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/17 16:32
 * @Version: 1.0
 */
@SpringBootTest
public class PublisherTest {


    private final static String QUEUE_NAME = "hello";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //Simple Queue
    @Test
    public void testSendMessage() throws IOException, TimeoutException, InterruptedException {
        // 1.建立连接

        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.202.131");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        //enable Publisher Confirms on a Channel
        channel.confirmSelect();

        // 3.创建队列
        channel.queueDeclare( QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 10000; i++) {
            String message = "Hello World!";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

            //publish a message as usual and wait for its confirmation
            channel.waitForConfirmsOrDie(5_000);
            System.out.println(" [x] Sent '" + message + "'");
        }

        // 5.关闭通道和连接
        channel.close();
        connection.close();

    }

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        // 1.消息体
        String message = "hello,spring!!";
        // 2.全局唯一的消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 添加callback
        correlationData.getFuture().addCallback((result) -> {
                    if (result.isAck()) {
                        // 3.1.ack，消息成功 (消息成功投递到Exchanger)
                        System.err.println("消息发送成功, ID:{}"+ correlationData.getId());
                    } else {
                        // 3.2.nack，消息失败 (消息没有成功投递到Exchanger)
                        System.err.println("消息发送失败, ID:{}, 原因{}"+correlationData.getId()+result.getReason());
                    }
                },
                ex -> System.err.println("消息发送异常, ID:{}, 原因{}"+ correlationData.getId()+ex.getMessage())
        );


        // 4.发送消息
        rabbitTemplate.convertAndSend(QUEUE_NAME, (Object) message, correlationData);

        // 休眠一会儿，等待ack回执
        Thread.sleep(2000);
    }

}
