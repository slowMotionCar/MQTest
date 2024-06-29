package com.example.mqtest.demos.utils;

/**
 * @ClassName: ConstantUtil
 * @Description: TODO
 * @Author: zzl
 * @Date: 2024/6/29 13:52
 * @Version: 1.0
 */
public class ConstantUtil {
    //常量

        //消息队列实现 RPC（远程过程调用）模型 之 服务器端----------------
        //消息队列
        public final static String RPC_QUEUE = "rpc_queue";


        // ------------topic类型的Exchange，需要的相关常量----------
        public final static String QUEUET01 = "qt_01";
        public final static String QUEUET02 = "qt_02";

        // topic 通配符类型的 Exchange
        public static final String EXCHANGE_NAME_TOPIC = "myex03.topic";

        // Exchange 绑定 Queue 队列的路由key  ，通配符类型      *：匹配一个单词。#：匹配零个或多个单词。
        public static final String[] ROUTING_TOPIC_PATTERNS = {"*.crazyit.*", "*.org", "edu.#"};

        // 生产者发送消息给Excahnge携带的路由key
        public static final String[] ROUTING_TOPIC_KEYS = { "www.crazyit.org", "www.crazyit.cn",
                "edu.crazyit.org", "crazyit.org", "fkjava.org", "edu.fkjava.org", "edu.fkjava", "edu.org"};

        //-------------------------------------------------------

        // 消息队列的名称
        public final static String QUEUE01 = "queue_01";
        public final static String QUEUE02 = "queue_02";
        // Exchange的名称
        public static final String EXCHANGE_NAME = "myex02.direct";
        // 三个路由key定义成一个数组的名称
        public static final String[] ROUTING_KEYS = {"info", "error", "warning"};



}
