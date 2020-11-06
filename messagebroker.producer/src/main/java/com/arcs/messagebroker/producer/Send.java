package com.arcs.messagebroker.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
public class Send {

	// DECLARAÇÃO DA FILA QUE O RABBIT MQ IRÁ RECEBER
	// O NOME DA FILA SERÁ IGUAL NO RECEBEDOR
	private final static String QUEUE_NAME = "RABBIT_MQ_QUEUE";
	
	// ENDEREÇO DO SERVIDOR ONDE ESTÁ O RABBIT MQ
	private final static String HOST_ADDRESS = "localhost";

	public void sendMessage(String message) throws Exception {
		
		// CLASSE PARA FACILITAR A CONEXÃO COM O RABBIT MQ
		ConnectionFactory factory = new ConnectionFactory();
		
		// DEFININDO O HOST
		factory.setHost(HOST_ADDRESS);
		
		// UTILIZANDO O TRY-WITH-RESOURCES PARA QUE A CONEXÃO FECHE AUTOMATICAMENTE AO ENVIAR A MENSAGEM
		// AS CLASSES IMPLEMENTAM JAVA.IO.CLOSEABLE
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

			// DECLARAÇÃO DA FILA
			// ELA SÓ SERÁ CRIADA SE AINDA NÃO EXISTIR
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			// MENSAGEM A SER ENVIADA
			String mensagem = String.join ( "" , message);
			
			// PUBLICANDO A MENSAGEM PARA O RABBIT MQ
			// O CONTEÚDO DA MENSAGEM É UMA MATRIZ DE BYTES
			// ENTÃO PODE-SE CODIFICAR O QUE QUISER
			channel.basicPublish ( "" , QUEUE_NAME , null , message.getBytes ()); 

			// LOG
			System.out.println ( "[x] Enviado '" + mensagem + "'" );
		}
	}
}
