package com.liez.rabbitMq.test01;

import com.liez.rabbitMq.util.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther liez
 * @Date 21:07 2022/3/10
 * 简单队列一对一生产者
 */
@Slf4j
public class snedMessage01 {

	private static String QUENE_NAME = "quene_test_01";

	public static void main(String[] args) throws IOException, InterruptedException {
		Connection mqConnection = MqConnectUtil.getMqConnection();
		Channel channel = mqConnection.createChannel();
		channel.queueDeclare(QUENE_NAME, true, false, false, null);
		String msg = "hello_mq";
		for (int i = 0; i < 100; i++) {
			Thread.sleep(1000);
			msg = msg + "-------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			channel.basicPublish("", QUENE_NAME, null, msg.getBytes());
			log.info("mq发送消息：{}", msg);
		}
		channel.close();
		mqConnection.close();
	}
}
