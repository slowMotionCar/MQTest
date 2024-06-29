package com.example.mqtest.demos.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @ClassName: SpringRabbitListener
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/17 16:28
 * @Version: 1.0
 */

@Component
public class SpringRabbitListener {

    private final static String QUEUE_NAME = "hello";
    private final static String SUB_QUEUE1_NAME = "test.subQueue1";
    private final static String SUB_QUEUE2_NAME = "test.subQueue2";
    private final static String DIR_QUEUE1_NAME = "test.directQueue1";
    private final static String DIR_QUEUE2_NAME = "test.directQueue2";
    private final static String DIR_QUEUE2_WZX = "test.directQueueWZX";
    private final static String DIR_QUEUE2_YKL = "test.directQueueYKL";
    private final static String DIR_TOPIC_WYF = "test.topicQWYF";
    private final static String DIR_TOPIC_HCY = "test.topicQHCY";


    //simpleQ监听
    /*
    @RabbitListener(queues = QUEUE_NAME)
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
    }
     */

    //workQ监听1
    @RabbitListener(queues = QUEUE_NAME)
    public void listenWorkQueue1(String msg) throws InterruptedException {
        throw new RuntimeException("Simulated message processing failure");
//        System.err.println("消费者1........接收到消息：【" + msg + "】" + LocalTime.now());
//        Thread.sleep(200);
    }

    /*
    //workQ监听2
    @RabbitListener(queues = QUEUE_NAME)
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
//        Thread.sleep(200);
    }

     */

    //sub Q1
    @RabbitListener(queues = SUB_QUEUE1_NAME)
    public void listensubQueue1(String msg) throws InterruptedException {
        System.err.println("消费者1在订阅SUB_QUEUE1_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(2000);
    }

    //sub Q2
    @RabbitListener(queues = SUB_QUEUE2_NAME)
    public void listensubQueue3(String msg) throws InterruptedException {
        System.err.println("消费者3在订阅SUB_QUEUE2_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(5000);
    }

    //direct Q1 fq在监听
    @RabbitListener(queues = DIR_QUEUE1_NAME)
    public void listendirQ1fq(String msg) throws InterruptedException {
        System.err.println("方琪在订阅DIR_QUEUE1_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    //direct Q1 wyf 在监听
    @RabbitListener(queues = DIR_QUEUE1_NAME)
    public void listendirQ1wyf(String msg) throws InterruptedException {
        System.err.println("吴羽霏在订阅DIR_QUEUE1_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    //direct Q1 hcy 在监听
    @RabbitListener(queues = DIR_QUEUE2_NAME)
    public void listendirQ1hcy(String msg) throws InterruptedException {
        System.err.println("黄楚茵在酒吧喝酒的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }


    //direct Q2 hcy 在监听
    @RabbitListener(queues = DIR_QUEUE2_WZX)
    public void listendirQ1wzx(String msg) throws InterruptedException {
        System.err.println("王姊歆在订阅DIR_QUEUE2_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    //direct Q2 hcy 在监听
    @RabbitListener(queues = DIR_QUEUE2_YKL)
    public void listendirQ1ykl(String msg) throws InterruptedException {
        System.err.println("杨可璐在订阅DIR_QUEUE2_NAME的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    /***
    * @Description //topic话题监听
    * @Param
    * @return
    **/
    //direct Q2 hcy 在监听
    @RabbitListener(queues = DIR_TOPIC_WYF)
    public void listenTopicWYF(String msg) throws InterruptedException {
        System.err.println("吴羽霏在睡觉的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    //direct Q2 hcy 在监听
    @RabbitListener(queues = DIR_TOPIC_HCY)
    public void listenTopicHCY(String msg) throws InterruptedException {
        System.err.println("黄楚茵在喝酒的时候........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

}
