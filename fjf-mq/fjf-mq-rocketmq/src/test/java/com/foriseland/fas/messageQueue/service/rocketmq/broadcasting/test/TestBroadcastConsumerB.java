package com.foriseland.fas.messageQueue.service.rocketmq.broadcasting.test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.broadcasting.BroadcastConsumerB;

@Component
public class TestBroadcastConsumerB {
	 
	@SuppressWarnings("resource")
	public static void main(String[] args) throws MQClientException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		BroadcastConsumerB bcB = context.getBean(BroadcastConsumerB.class);

		// 消息模型，支持以下两种：集群消费(clustering)，广播消费(broadcasting)
		bcB.setMessageModel(MessageModel.BROADCASTING);
		// bcB.setConsumerGroup("192.168.2.31:9876","broadcast");
		bcB.setConsumerGroup("-", "broadcast");
		bcB.subscribe("broadcast", "Tag1 || Tag2 || Tag3 || TagA");
		MessageListenerConcurrently lister = new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) { 
				MessageExt msg = msgs.get(0);
				try {
					bcB.getLogger().info(Thread.currentThread().getName() + " consumerB Receive New Messages: " + msgs + "%n"); 
					String m = new String(msg.getBody());
					bcB.getLogger().info(m);
				} catch (Exception e) {
					if (msg.getReconsumeTimes() == bcB.getRocketMQPushConsumer().getMessageDelayLevel()) {
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}else{
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
				} 
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		};
		bcB.registerMessageListener(lister);
		bcB.getLogger().info("Broadcast ConsumerB Started.%n");
	}

	public static void main1(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_2");
		String namesrvAddr = "192.168.2.31:9876";
		namesrvAddr = "192.168.2.31:9876;192.168.2.32:9876;192.168.2.33:9876";
		consumer.setNamesrvAddr(namesrvAddr);
		// 消息模型，支持以下两种：集群消费(clustering)，广播消费(broadcasting)
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.setConsumerGroup("broadcast");
		consumer.subscribe("TopicTest1", "Tag1 || Tag2 || Tag3");
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf(Thread.currentThread().getName() + " consumerB Receive New Messages: " + msgs + "%n");
				MessageExt me = msgs.get(0);
				String m = new String(me.getBody());
				System.out.println(m);

				try {
					TimeUnit.MILLISECONDS.sleep(1000 * 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.out.printf("Broadcast ConsumerB Started.%n");
	}

}
