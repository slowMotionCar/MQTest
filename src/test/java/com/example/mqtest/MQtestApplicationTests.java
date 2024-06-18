package com.example.mqtest;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
class MQtestApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    RedisTemplate redisTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void redisTest() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hello", "Hello, World!");
    }

    @Test
    public void redisTestRead() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get("hello");
        System.out.println((String)o);
    }


}
