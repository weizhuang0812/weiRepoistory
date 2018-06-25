package com.foriseland.fas.messageQueue.service.rocketmq.immediate.test;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.immediate.ImmediateRocketConsumer;

@Component
public class TestImmediateRocketConsumer {
 
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws MQClientException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		ImmediateRocketConsumer immediateRocketConsumer = context.getBean(ImmediateRocketConsumer.class);

		// immediateRocketConsumer.setConsumerGroup("192.168.2.31:9876","broadcast");
		immediateRocketConsumer.setConsumerGroup("-", "immediateRocketConsumer");
		immediateRocketConsumer.subscribe("TopicTest1", "");
		immediateRocketConsumer.subscribe("TopicTest-zhangyan", "");
		immediateRocketConsumer.subscribe("TopicTest1 | TopicTest-zhangyan", "");
		int consumeMessageBatchMaxSize = immediateRocketConsumer.getRocketMQPushConsumer().getRocketMqClientConfig().getRocketConfig().getConsumeMessageBatchMaxSize();
		immediateRocketConsumer.getRocketMQPushConsumer().newInstance().setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
		
		MessageListenerConcurrently lister = new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				immediateRocketConsumer.getLogger().info(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());

				MessageExt msg = msgs.get(0);
				immediateRocketConsumer.getLogger().info( msg.getTopic() );
				immediateRocketConsumer.getLogger().info(	msg.getReconsumeTimes() );
			
				try {

					if (msg.getTopic().equals("TopicTest-zhangyan")) {
						// 执行TopicTest1的消费逻辑
						if (msg.getTags() != null && msg.getTags().equals("Tag1")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest-zhangyan:==Tag1===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag2")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest-zhangyan:==Tag2===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag3")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest-zhangyan:==Tag3===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else {
						}
						
					} else if (msg.getTopic().equals("TopicTest1")) {
						// 执行TopicTest1的消费逻辑
						if (msg.getTags() != null && msg.getTags().equals("Tag1")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest1:==Tag1===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag2")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest1:==Tag2===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag3")) {
							// 执行Tag1的消费
							immediateRocketConsumer.getLogger().info("TopicTest1:==Tag3===");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						} else {
						}
						
					} else if (msg.getTopic().equals("TopicTest2")) {
						if (msg.getTags() != null && msg.getTags().equals("Tag2")) {
							// 执行Tag2的消费
							immediateRocketConsumer.getLogger().info("TopicTest2:=====");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						}else{
							
						}

					} else if (msg.getTopic().equals("TopicTest3")) {
						if (msg.getTags() != null && msg.getTags().equals("Tag3")) {
							// 执行Tag2的消费
							immediateRocketConsumer.getLogger().info("TopicTest3:=====");
							immediateRocketConsumer.getLogger().info(new String(msg.getBody()));
						}
					} else {

					}
				} catch (Exception e) {
					if (msg.getReconsumeTimes() == immediateRocketConsumer.getRocketMQPushConsumer().getMessageDelayLevel()) {
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}else{
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
				}

				/*try {
					TimeUnit.MILLISECONDS.sleep((long) (1000 * 0.02));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

			}
		};

		/**
		 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		 */
		immediateRocketConsumer.registerMessageListener(lister);
		immediateRocketConsumer.getLogger().info("immediate Consumer Started.%n");
	}

}
