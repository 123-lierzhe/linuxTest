package com.liez.rabbitMq.test02SendAndReceiveConfrim;

import com.liez.rabbitMq.util.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * liez
 *https://blog.csdn.net/molihuakai_118/article/details/107196871
 * @date 2022/3/11
 * 手动确认消费模式
 */
public class ReceiveMessage02 {

    private static String QUENE_NAME = "quene_test_01";

    public static void main(String[] args) throws IOException {
        Connection mqConnection = MqConnectUtil.getMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.queueDeclare(QUENE_NAME, true, false, false, null);
        // 即在非自动确认消息的前提下，如果一定数目的消息(通过基于consume或者channel设Qos的值)未被确认前，不进行消费新的消息。
        //prefetchSize: 0
        //prefetchCount:会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，即一-旦有N个消息还没有ack,则该consumer将block掉，直到有消息ack
        //global: true\false 是否将上面设置应用于channel。简单点说，就是上面限制是channe|级别的还是consumer级别
        channel.basicQos(0, 10, false);
        channel.basicConsume(QUENE_NAME, false, new MyConsume(channel));



    }
}
