package com.arcs.messagebroker.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	 
	public static void main(String[] args) throws Exception {
		Send send = new Send();
		send.sendMessage("1 - Teste .");
		send.sendMessage("2 - Teste ..");
		send.sendMessage("3 - Teste ...");
		send.sendMessage("4 - Teste ....");
		send.sendMessage("5 - Teste .....");
		send.sendMessage("6 - Teste ......");
	}
}
