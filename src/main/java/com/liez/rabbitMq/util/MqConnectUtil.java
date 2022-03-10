package com.liez.rabbitMq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @Auther liez
 * @Date 21:01 2022/3/10
 */
public class MqConnectUtil {
	public static Connection getMqConnection() throws IOException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("39.107.13.68");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/test");
		connectionFactory.setUsername("liez");
		connectionFactory.setPassword("liez");
		Connection connection = connectionFactory.newConnection();
		return connection;
	}
}
