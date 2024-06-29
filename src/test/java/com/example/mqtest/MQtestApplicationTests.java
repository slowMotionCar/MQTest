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


    //SimpleQ 前提在于必须存在队列则可用
    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "hello";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }


    @Test
    //SUB Queue
    public void testSubQ() {
        // 队列名称
        String exchangeName = "test.subExchange";
        // 消息
        String message = "hello, this is subExchange!";
        // 发送消息
        for (int i = 0; i < 6; i++) {
            rabbitTemplate.convertAndSend(exchangeName, "" ,message);
        }
    }

    @Test
    //SUB Queue with routing key
    public void testSubQWithRoutingKeyAskHcy() {
        // 队列名称
        String exchangeName = "test.directExchange";
        // 消息
        String message = "晚上有空出来喝酒吗？";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "hcy" ,message);
    }
    @Test
    //SUB Queue with routing key
    public void testSubQWithRoutingKeyAskBEQFfq() {
        // 队列名称
        String exchangeName = "test.directExchange";
        // 消息
        String message = "晚上有空出来吃饭吗？";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "fq" ,message);
    }
    @Test
    //SUB Queue with routing key
    public void testSubQWithRoutingKeyAskBEQFwyf() {
        // 队列名称
        String exchangeName = "test.directExchange";
        // 消息
        String message = "晚上有空出来吃饭吗？";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "wyf" ,message);
    }

    @Test
    //SUB Queue with routing key
    public void testSubQWithRoutingKeyAskXL() {
        // 队列名称
        String exchangeName = "test.directExchange";
        // 消息
        String message = "晚上有空出来玩耍吗？";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "xl" ,message);
    }

    /***
    * @Description //话题订阅
    * @Param
    * @return
    **/
    @Test
    //SUB Queue with routing key
    public void testTopics() {
        //抽烟喝酒烫头
        // 队列名称
        String exchangeName = "test.topicExchange";
        // 消息
        String message = "晚上有空出来抽烟喝酒烫头吗？";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "喝酒.抽烟.烫头" ,message);
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
