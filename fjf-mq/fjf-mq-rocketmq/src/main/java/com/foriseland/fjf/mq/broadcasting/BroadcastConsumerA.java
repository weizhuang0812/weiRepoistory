package com.foriseland.fjf.mq.broadcasting;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketMQPushConsumer;

@Component
public class BroadcastConsumerA {
	
	private Logger logger = Logger.getLogger(BroadcastConsumerA.class);
	
	@Autowired
	private RocketMQPushConsumer rocketMQPushConsumer;

	public BroadcastConsumerA() {
	}
	
	public RocketMQPushConsumer getRocketMQPushConsumer() {
		return rocketMQPushConsumer;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setMessageModel(MessageModel model) {
		if (null == model) {
			rocketMQPushConsumer.newInstance().setMessageModel(MessageModel.BROADCASTING);
		}
		rocketMQPushConsumer.newInstance().setMessageModel(model);
	}

	public void setConsumerGroup(String namesrvAddr, String group) throws MQClientException {
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}

		if ("-".equals(namesrvAddr)) {
		} else {
			rocketMQPushConsumer.newInstance().setNamesrvAddr(namesrvAddr);
		}

		if (null == group || "".equals(group)) {
			group = "broadcast";
		}
		rocketMQPushConsumer.newInstance().setConsumerGroup(group);
	}

	public void subscribe(String topic, String subExpression) throws MQClientException {
		rocketMQPushConsumer.newInstance().subscribe(topic, subExpression);
	}

	public void registerMessageListener(MessageListenerConcurrently lister) throws MQClientException {
		rocketMQPushConsumer.newInstance().registerMessageListener(lister);
		rocketMQPushConsumer.newInstance().start();
	}

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
					bcA.getLogger().info(Thread.currentThread().getName() + " consumerA Receive New Messages: " + msgs + "%n");
			
					String m = new String(msg.getBody());
					bcA.getLogger().info(m);
				} catch (Exception e) {
					if (msg.getReconsumeTimes() == bcA.rocketMQPushConsumer.getMessageDelayLevel()) {
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}else{
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
