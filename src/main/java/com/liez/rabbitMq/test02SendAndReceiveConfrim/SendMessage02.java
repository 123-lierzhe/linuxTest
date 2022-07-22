package com.liez.rabbitMq.test02SendAndReceiveConfrim;

import com.liez.rabbitMq.util.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * liez
 *
 * @date 2022/3/11
 * 手动发送确认模式
 */
@Slf4j
public class SendMessage02 {

    private static String QUENE_NAME = "quene_test_01";

    public static void main(String[] args) throws IOException, InterruptedException {
        Connection mqConnection = MqConnectUtil.getMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.queueDeclare(QUENE_NAME, true, false, false, null);
        //开启发送确认
        channel.confirmSelect();
        while (true) {
            String messge = "hello_confirm_mq";
            messge = messge + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Thread.sleep(1000);
            channel.basicPublish("", QUENE_NAME, null, messge.getBytes());
            log.info("生产者发送消息:{}", messge);
            //添加一个确认监听
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    //log.info("----------------yes send ack---------------:{}", deliveryTag);
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    log.info("----------------no send ack---------------：{}", deliveryTag);
                }
            });
        }

    }
}
