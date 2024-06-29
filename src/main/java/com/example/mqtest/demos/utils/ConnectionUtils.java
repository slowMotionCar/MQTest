package com.example.mqtest.demos.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: ConnectionUtils
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 13:52
 * @Version: 1.0
 */
public class ConnectionUtils {

    //连接工具

    //获取连接的方法
    public static Connection getConnection() throws IOException, TimeoutException {
        //创建连接工厂----这个ConnectionFactory源码可以看出有构造器，所以直接new一个出来
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接信息
        connectionFactory.setHost("192.168.202.131");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //从连接工厂获取连接
        Connection connection = connectionFactory.newConnection();
        //返回连接
        return connection;
    }

}
