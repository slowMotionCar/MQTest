package com.example.mqtest.demos.server;

import com.example.mqtest.demos.utils.ConnectionUtils;
import com.example.mqtest.demos.utils.ConstantUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: Server
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 13:54
 * @Version: 1.0
 */
public class Server {

    //消息队列实现 RPC（远程过程调用）模型 之 服务器端
    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建连接
        Connection conn = ConnectionUtils.getConnection();

        //2、通过Connection获取Channel。
        Channel channel = conn.createChannel();

        //3、使用系统自动创建的默认Exchange，无需声明Exchange

        //消息队列设置为独占（exclusive:true）--------------------------------------------------------------------------------------声明消息队列
        channel.queueDeclare(ConstantUtil.RPC_QUEUE,
                true,   /* 是否持久化 */
                true,  /* 是否只允许只有这个消息队列的消息消费者才可以消费这个消息队列的消息 */
                false, /* 是否自动删除 */
                null); /* 指定这个消息队列的额外参数属性 */

        //不需要关闭资源，因为它也要监听自己消费消息的队列
        //4、调用Channel 的 basicConsume()方法开始消费消息----------------------------------------------------------------------------1、服务端监听并消费消息
        channel.basicConsume(
                ConstantUtil.RPC_QUEUE, /* 消费这个名字的消费队列里面的消息 */
                true,
                new DefaultConsumer(channel) {
                    //处理消息：当这个 ConstantUtil.RPC_QUEUE 消息队列收到消息的时候，这个方法就会被触发。重写这个方法：
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope /*消息所在的信封,存放消息的exchange、路由key这些*/,
                                               AMQP.BasicProperties properties /* 消息的那些属性 */,
                                               byte[] body /*body：消息的消息体*/) throws IOException {
                        //把消息体中的消息拿出来，此处读取到的消息，就相当于调用参数-------------------------------------------------------2、服务端获取消息队列的消息
                        String param = new String(body, StandardCharsets.UTF_8);
                        //之前只需要用到消息，现在需要额外读取消息里面携带的两个属性：reply_to 和 correlation_id
                        //消息的属性都存放在 AMQP.BasicProperties 这个属性里面，从这个属性获取 reply_to 和 correlation_id
                        String replyTo = properties.getReplyTo();
                        System.err.println("replyTo:  " + replyTo);

                        String correlationId = properties.getCorrelationId();
                        System.err.println("correlationId:  " + correlationId);

                        //调用服务器的处理消息的方法，最终得到处理后的结果。该方法可以是任意的业务处理,该方法的返回值result是要被送回客户端的。------3、服务端处理消费消息
                        String result = format(param);

                        //printf：格式化输出函数   %s：输出字符串  %n：换行
                        System.err.printf("服务端 收到来自Exchange为【%s】、路由key为【%s】的消息，消息内容为%s%n",
                                envelope.getExchange(), envelope.getRoutingKey(), param);

                        //发送消息的方法，需要把返回值result发送回客户端-------------------------------------------------------4、服务端处理消费完的消息返回客户端的操作
                        channel.basicPublish(
                                "", /* 使用默认的Exchange */
                                replyTo,/* 此处的routing key 应该填 reply_to 属性;  reply_to: 该属性指定了服务器要将返回的消息送回到哪个队列 */

                                //把从客户端的 AMQP.BasicProperties 属性获取到的correlationId，再作为参数传回去，用于客户端和服务器的匹配。
                                new AMQP.BasicProperties()
                                        .builder()
                                        .correlationId(correlationId) /* 也需要返回额外的 correlation_id，要与从客户端消息中读取的 correlation_id 完全一样 */
                                        .deliveryMode(2) /* 设置这个消息是持久化类型的 */
                                        .build(), /*这个.build()的作用就是构建得到这个 BasicProperties 对象，这个对象就包含了 correlationId 属性
                                                   因为服务器端返回的消息一定要有这个correlationId。 */
                                result.getBytes(StandardCharsets.UTF_8)
                        );
                    }
                });
    }

    //模拟服务器端消费消息要做的处理业务逻辑操作
    public static String format(String name) {
        //此处模拟让服务器处理这里的业务有快有慢的情况，看correlation_id 能不能还是把数据对应上
        int rand = (new Random().nextInt(40) + 20) * 30;
        try {
            Thread.sleep(rand);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "《" + name + "》";
    }


}
