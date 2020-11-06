package com.arcs.messagebroker.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
public class Consumer {

	// DECLARAÇÃO DA FILA QUE O RABBIT MQ IRÁ RECEBER
	// O NOME DA FILA SERÁ IGUAL NO RECEBEDOR
	private final static String QUEUE_NAME = "RABBIT_MQ_QUEUE";

	// ENDEREÇO DO SERVIDOR ONDE ESTÁ O RABBIT MQ
	private final static String HOST_ADDRESS = "localhost";

	public void consumeMessage() throws Exception {

		// CLASSE PARA FACILITAR A CONEXÃO COM O RABBIT MQ
		ConnectionFactory factory = new ConnectionFactory();

		// DEFININDO O HOST
		factory.setHost(HOST_ADDRESS);

		// INTERFACE DE CONEXÃO
		Connection connection = factory.newConnection();

		// INTERFACE PARA COMUNICAR COM O RABBIT MQ
		Channel channel = connection.createChannel();

		// DECLARAÇÃO DA FILA
		// ELA SÓ SERÁ CRIADA SE AINDA NÃO EXISTIR
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// LOG
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		// QUANTIDADE DE MENSAGENS QUE IRÁ RECEBER POR VEZ
		channel.basicQos(1);

		// FUNÇÃO QUE SERÁ ACIONADA SEMPRE QUE RECEBERMOS UMA MENSAGEM
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {

			// CAPTURANDO O CORPO DA MENSAGEM
			String message = new String(delivery.getBody(), "UTF-8");

			// LOG
			System.out.println(" [x] Received '" + message + "'");
			
			try {
				// QUANDO RECEBER A MENSAGEM, FAÇA O QUE TIVER QUE SER FEITO
				doWork(message);
			} catch (InterruptedException e) {
				// LOG
				System.out.println(" [x] Error");
			} finally {
				// LOG
				System.out.println(" [x] Done");
				
				// AVISANDO O RABBIT MQ QUE O A MENSAGEM FOI RECEBIDA POR COMPLETO
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		};

		// FUNÇÃO QUE SERÁ ACIONADA SEMPRE QUE A MENSAGEM FOR CANCELADA
		CancelCallback cancelCallback = (consumerTag) -> {
			
		};

		// CONFIRMADOR DE MENSAGEM RECEBIDA
		// CASO TRUE, ASSIM QUE O RABBIT MQ NOS ENTREGA A MENSAGEM
		// ELE JÁ MARCA PARA EXCLUSÃO LÁ
		// CASO FALSE, O RABBIT ESPERA UMA CONFIRMAÇÃO NOSSA 
		// DE QUE A MENSAGEM FOI RECEBIDA
		
		// ACK = ACKNOWLEDGE = RECONHECIMENTO DA MENSAGEM RECEBIDA 
		boolean autoAck = false;
		
		// INICIA UM CONSUMIDOR, PASSANDO A FILA, O AUTOACK E O CALLBACK DE ENTREGA E CANCELAMENTO
		channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
	}

	// CLASSE SIMULANDO O TEMPO DE PROCESSAMENTO DE UMA MENSAGEM
	// A CADA '.' (PONTO) NA MENSAGEM, O "PROCESSAMENTO" DEMORA MAIS 1 SEGUNDO
	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}
}
