package com.arcs.messagebroker.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws Exception {
		Consumer consumer = new Consumer();
		consumer.consumeMessage();
	}
}
