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

	public static void main(String[] args) throws IOException {
		Connection mqConnection = MqConnectUtil.getMqConnection();
		Channel channel = mqConnection.createChannel();
		channel.queueDeclare(QUENE_NAME,true, false, false, null);
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		channel.basicConsume(QUENE_NAME, false, new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				log.info("接收消息：{}，时间：{}",new String(body), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//消息确认
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		});
		channel.close();
		mqConnection.close();
	}
}
