package com.example.mqtest.demos.client;

import com.example.mqtest.demos.utils.ConnectionUtils;
import com.example.mqtest.demos.utils.ConstantUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: Client
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 13:56
 * @Version: 1.0
 */
public class Client {

    //消息队列实现 RPC（远程过程调用）模型 之 客户端

    // paramMap 保存了 correlationid 与 参数（消息）之间的对应关系
    public static Map<String, String> paramMap = new ConcurrentHashMap<>();

    //客户端发送的消息（参数）
    public static String[] params = new String[]{"吴羽霏", "方琪", "黄楚茵", "杨可璐"};

    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建连接工厂，设置连接信息，然后再通过连接工厂获取连接
        Connection conn = ConnectionUtils.getConnection();

        //2、通过Connection获取Channel 消息通道
        Channel channel = conn.createChannel();

        //3、调用 Channel 的 queueDeclare() 方法声明队列，声明一个有 RabbitMQ 自动创建的、自动命名的、持久化的、独享的、会自动删除的【默认队列】
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();
        System.out.println("declareOk  " + declareOk);

        //.getQueue() 用于得到默认队列的返回值，也就是默认队列的名字，之前声明是我们自己设置队列名，这里用默认的队列，就用.getQueue() 得到队列名。
        String queueName = declareOk.getQueue();
        System.out.println("queueName: " + queueName);

        //4、调用Channel 的 basicConsume()方法开始处理消费消息-----------------------------------------------------------------2、客户端监听服务端处理完消息后返回来的消息
        channel.basicConsume(
                queueName /*消费这个消费队列里面的消息*/,
                true /*消息的确认模式：是否自动确认该消息已经被消费完成并返回确认消息给消息队列*/,
                new DefaultConsumer(channel) {
                    //处理消息：当这个消息队列收到消息的时候，这个方法就会被触发。重写这个方法：
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope /*消息所在的信封,存放消息的exchange、路由key这些*/,
                                               AMQP.BasicProperties properties /*消息的那些属性*/,
                                               byte[] body /*body：消息的消息体*/) throws IOException {
                        //把消息体中的消息拿出来
                        String resultMessage = new String(body, StandardCharsets.UTF_8);

                        //此处，需要指定每个返回值对应的是哪个参数，靠的就是correlation_id
                        String correlationId = properties.getCorrelationId();

                        //根据服务器端返回的消息中的correlation_id 获取对应的参数
                        String param = paramMap.get(correlationId);

                        System.err.println("客户端发出去的消息内容：" + param + "  ， 服务端处理后返回来的消息内容：" + resultMessage);

                        //printf：格式化输出函数   %s：输出字符串  %n：换行
                        System.out.printf("客户端 收到来自Exchange为【%s】、路由key为【%s】的消息，消息内容为%s%n",
                                envelope.getExchange(), envelope.getRoutingKey(), resultMessage);

                        //得到服务器的返回值之后，整个调用过程就完成了，此时就应该从 Map 中删除这组 key-value对了( correlationId 与 参数的对应关系 )。
                        paramMap.remove(correlationId);
                    }
                }
        );

        //客户端发送消息---------------------------------------------------------------------------代码运行后先执行这段--------------------1、客户端发送消息

        //创建了一个map，发送后就把数据保存到map里面
        //当获取返回值的时候根据发送的id比对然后删除里面所需内容
        for (int i = 0; i < params.length; i++) {
            paramMap.put(i + "", params[i]);

            channel.basicPublish("", /* 使用默认的Exchange */
                    ConstantUtil.RPC_QUEUE, /* 客户端发送消息携带的路由key是服务端监听的消息队列的名字，且使用了默认的Exchange，这就意味着消息会被发送给服务器监听的那个消息队列 */
                    new AMQP.BasicProperties().builder()
                            .correlationId(i + "")  /* 设置 correlation_id 属性;  correlation_id：该属性指定了服务器返回的消息也要添加相同的correlation_id属性*/
                            .replyTo(queueName)     /* reply_to: 该属性指定了服务器要将返回的消息送回到哪个队列 , 设置 reply_to 属性 */
                            .deliveryMode(2)  /* 持久化消息 */
                            .build(),          /* 构建这个BasicProperties对象，这个对象主要存这个correlationId属性 */
                    params[i].getBytes(StandardCharsets.UTF_8)
            );
        }
    }


}
