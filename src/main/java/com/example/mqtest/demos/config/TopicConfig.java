package com.example.mqtest.demos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: TopicConfig
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 11:29
 * @Version: 1.0
 */
@Configuration
public class TopicConfig {
    /**
     * 声明交换机
     *
     * @return Fanout类型交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("test.topicExchange");
    }

    /**
     * 第1个队列
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue("test.topicQWYF", true);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingTopic1(Queue topicQueue1, TopicExchange topicExchange) {
        Binding wyf = BindingBuilder.bind(topicQueue1).to(topicExchange).with("*.抽烟.烫头");
        return wyf;
    }

    /**
     * 第2个队列
     */
    @Bean
    public Queue topicQueue2() {
        return new Queue("test.topicQHCY",true);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingTopic3(Queue topicQueue2, TopicExchange topicExchange) {
        Binding hcy = BindingBuilder.bind(topicQueue2).to(topicExchange).with("喝酒.#");
        return hcy;
    }
}
