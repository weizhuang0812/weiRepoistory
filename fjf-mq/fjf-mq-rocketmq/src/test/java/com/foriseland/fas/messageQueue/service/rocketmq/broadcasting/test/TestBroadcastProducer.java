package com.foriseland.fas.messageQueue.service.rocketmq.broadcasting.test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.broadcasting.BroadcastProducer;


@Component
public class TestBroadcastProducer {
 
	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		 
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		BroadcastProducer broadcast = context.getBean(BroadcastProducer.class);
 
		// broadcast.setProducerGroup("192.168.2.31:9876","broadcast"); 
		// broadcast.setProducerGroup("127.0.0.1:9876","broadcast"); ; 
		// broadcast.setProducerGroup("192.168.3.104:9876","broadcast"); 
		broadcast.setProducerGroup("-","broadcast"); 
		
		for (int i = 0; i <1; i++) {
			SendResult sendResult = broadcast.send("broadcast", "broadcast", "TagA", "orderId01", "hello consumer broadcast "+ i);

			TimeUnit.MILLISECONDS.sleep(1000 * 2);
		}
		broadcast.shutdown();
	}

	public static void main1(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		// DefaultMQProducer producer = RocketProducer.newInstance();
		
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		producer = new DefaultMQProducer();

		String namesrvAddr = "192.168.2.32:9876";
		// namesrvAddr="192.168.2.31:9876;192.168.2.32:9876;192.168.2.33:9876";
		producer.setNamesrvAddr(namesrvAddr);
		producer.setProducerGroup("broadcast");
		producer.setInstanceName("broadcast");
		producer.start();
		for (int i = 0; i < 20000; i++) {
			long time = System.currentTimeMillis();
			Message message = new Message("TopicTest1", "Tag1", "orderId01",
					("hello consumer broadcast " + time).getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message);
			System.out.printf("%s%n", sendResult);
			System.out.println();
			TimeUnit.MILLISECONDS.sleep(1000 * 1);
		}
		producer.shutdown();
	}
}
