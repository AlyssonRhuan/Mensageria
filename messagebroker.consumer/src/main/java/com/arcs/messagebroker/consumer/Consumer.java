package com.arcs.messagebroker.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
public class Consumer {

	private final static String QUEUE_NAME = "hello";

	public void consumeMessage() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		channel.basicQos(1);
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {

			String message = new String(delivery.getBody(), "UTF-8");

			System.out.println(" [x] Received '" + message + "'");
			try {
				doWork(message);
			} catch (InterruptedException e) {
				System.out.println(" [x] Error");
			} finally {
				System.out.println(" [x] Done");
			    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		};

		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
		});
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}
}
