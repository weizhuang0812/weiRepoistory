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

import com.foriseland.fjf.mq.broadcasting.BroadcastConsumerA;

@Component
public class TestBroadcastConsumerA {

	public static void main(String[] args) throws MQClientException {

		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);

		BroadcastConsumerA bcA = context.getBean(BroadcastConsumerA.class);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties");

		// 消息模型，支持以下两种：集群消费(clustering)，广播消费(broadcasting)
		bcA.setMessageModel(MessageModel.BROADCASTING);
		// bcA.setConsumerGroup("192.168.2.31:9876","broadcast");
		bcA.setConsumerGroup("-", "broadcast");
		bcA.subscribe("broadcast", "Tag1 || Tag2 || Tag3 || TagA");
		MessageListenerConcurrently lister = new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				MessageExt msg = msgs.get(0);
				try {
					bcA.getLogger()
							.info(Thread.currentThread().getName() + " consumerA Receive New Messages: " + msgs + "%n");

					String m = new String(msg.getBody());
					bcA.getLogger().info(m);
				} catch (Exception e) {
					if (msg.getReconsumeTimes() == bcA.getRocketMQPushConsumer().getMessageDelayLevel()) {
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					} else {
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		};
		bcA.registerMessageListener(lister);

		bcA.getLogger().info("Broadcast ConsumerA Started.%n");
	}

	public static void main1(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_2");
		String namesrvAddr = "192.168.2.32:9876";
		// namesrvAddr="192.168.2.31:9876;192.168.2.32:9876;192.168.2.33:9876";
		consumer.setNamesrvAddr(namesrvAddr);
		// 消息模型，支持以下两种：集群消费(clustering)，广播消费(broadcasting)
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.setConsumerGroup("broadcast");
		consumer.subscribe("TopicTest1", "Tag1 || Tag2 || Tag3");
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf(Thread.currentThread().getName() + " consumerA Receive New Messages: " + msgs + "%n");
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
		System.out.printf("Broadcast ConsumerA Started.%n");
	}

}
