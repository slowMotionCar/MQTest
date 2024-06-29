package com.example.mqtest.demos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: DirectConfig
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 10:05
 * @Version: 1.0
 */
@Configuration
public class DirectConfig {
    /**
     * 声明交换机
     *
     * @return Fanout类型交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("test.directExchange");
    }

    /**
     * 第1个队列
     */
    @Bean
    public Queue directQueue1() {
        return new Queue("test.directQueue1", true);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingDirect1(Queue directQueue1, DirectExchange directExchange) {
        Binding wyf = BindingBuilder.bind(directQueue1).to(directExchange).with("wyf");
        return wyf;
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingDirect2(Queue directQueue1, DirectExchange directExchange) {
        Binding fq = BindingBuilder.bind(directQueue1).to(directExchange).with("fq");
        return fq;
    }

    /**
     * 第2个队列
     */
    @Bean
    public Queue directQueue2() {
        return new Queue("test.directQueue2",true);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingDirect3(Queue directQueue2, DirectExchange directExchange) {
        Binding hcy = BindingBuilder.bind(directQueue2).to(directExchange).with("hcy");
        return hcy;
    }

    /***
    * @Description //实现目标，发送给fq，wyf，hcy的消息不发给xl
    * @Param
    * @return
    **/


    //创建新的exchange
    @Bean
    public FanoutExchange fanoutExchangeXL() {
        return new FanoutExchange("test.fanoutExchangeXL");
    }

    //把交换机1号绑定到交换机2号
    @Bean
    public Binding bindingToFilteredExchange(FanoutExchange fanoutExchangeXL, DirectExchange directExchange) {
        return BindingBuilder.bind(fanoutExchangeXL).to(directExchange).with("xl");
    }

    /**
     * 独立的队列给wzx
     */
    @Bean
    public Queue directQueueWZX() {
        return new Queue("test.directQueueWZX",true);
    }
    /**
     * 独立的队列给ykl
     */
    @Bean
    public Queue directQueueYKL() {
        return new Queue("test.directQueueYKL",true);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingDirectWZX(Queue directQueueWZX, FanoutExchange fanoutExchangeXL) {
        return BindingBuilder.bind(directQueueWZX).to(fanoutExchangeXL);
    }
    @Bean
    public Binding bindingDirectYKL(Queue directQueueYKL, FanoutExchange fanoutExchangeXL) {
        return BindingBuilder.bind(directQueueYKL).to(fanoutExchangeXL);
    }


}
