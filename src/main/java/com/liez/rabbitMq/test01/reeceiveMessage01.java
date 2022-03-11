package com.liez.rabbitMq.test01;

import com.liez.rabbitMq.util.MqConnectUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Auther liez
 * @Date 21:20 2022/3/10
 * 简单队列一对一消费者
 */
@Slf4j
public class reeceiveMessage01 {

	private static String QUENE_NAME = "quene_test_01";

	public static void main(String[] args) throws IOException, InterruptedException {
		Connection mqConnection = MqConnectUtil.getMqConnection();
		Channel channel = mqConnection.createChannel();
		channel.queueDeclare(QUENE_NAME,true, false, false, null);
		//创建消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		channel.basicConsume(QUENE_NAME, true, queueingConsumer);

		while (true){
			QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
			log.info("接收到消息：{}", new String(delivery.getBody()));
		}
	}
}
