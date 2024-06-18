package com.example.mqtest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: PublisherTest
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/17 16:32
 * @Version: 1.0
 */
public class PublisherTest {


    private final static String QUEUE_NAME = "hello";
    @Test
    public void testSendMessage() throws IOException, TimeoutException, InterruptedException {
        // 1.建立连接

        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.0.92");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("zzl");
        factory.setPassword("123");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        channel.queueDeclare( QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 100; i++) {
            String message = "Hello World!";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        // 省略了写入操作
        // 5.关闭通道和连接
        channel.close();
        connection.close();

    }

}
