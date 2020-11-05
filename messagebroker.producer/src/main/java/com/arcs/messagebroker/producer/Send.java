package com.arcs.messagebroker.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
public class Send {

	private final static String QUEUE_NAME = "hello";

	public void sendMessage(String message) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String mensagem = String.join ( "" , message); 
			channel.basicPublish ( "" , QUEUE_NAME , null , message.getBytes ()); 
			System.out.println ( "[x] Enviado '" + mensagem + "'" );
		}
	}
}
