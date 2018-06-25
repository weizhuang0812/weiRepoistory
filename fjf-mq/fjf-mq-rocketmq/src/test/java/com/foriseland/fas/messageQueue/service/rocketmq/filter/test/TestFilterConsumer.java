package com.foriseland.fas.messageQueue.service.rocketmq.filter.test;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.filter.FilterConsumer;

@Component
public class TestFilterConsumer { 
	
	/*
	 * http://rocketmq.apache.org/docs/filter-by-sql92-example/
	 */
	public static void main(String[] args) throws MQClientException, IOException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		FilterConsumer consumer = context.getBean(FilterConsumer.class); 		 
		consumer.setConsumerGroup("-", "filter_consumer");
		System.setProperty("rocketmq.client.name", "filter_consumer");
		String instanceName = consumer.getRocketMQPushConsumer().newInstance().getInstanceName();
		consumer.getRocketMQPushConsumer().newInstance().setInstanceName("filter_consumer");
		instanceName = consumer.getRocketMQPushConsumer().newInstance().getInstanceName();
		// only subsribe messages have property a, also a >=0 and a <= 3
		// consumer.subscribe("topic-filter", MessageSelector.bySql("a between 0 and 3"));
		// String filterCode = MixAll.file2String(url);
		String filterCode = MixAll.file2String("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\java\\com\\foriseland\\fas\\messageQueue\\service\\rocketmq\\filter\\MessageFilterImpl.java");
        // System.out.println(filterCode);
        consumer.subscribe("filter", "com.foriseland.fas.messageQueue.service.rocketmq.filter.MessageFilterImpl", filterCode); 
		MessageListenerConcurrently listener = new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 for (MessageExt msg : msgs) {
					 try{
						 // Print approximate delay time period
						 consumer.getLogger().info("filter Receive message[msgId=" + msg.getMsgId() + "] " + (System.currentTimeMillis() - msg.getStoreTimestamp()) + "ms filter");
						 consumer.getLogger().info(msg.getUserProperty("a"));
                     
					} catch (Exception e) {
						if (msg.getReconsumeTimes() == consumer.getRocketMQPushConsumer().getMessageDelayLevel()) {
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						}else{
							return ConsumeConcurrentlyStatus.RECONSUME_LATER;
						}
					} 
                 } 
                 return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		};
		consumer.registerMessageListener(listener); 
		consumer.getLogger().info("Filter ConsumerB Started.%n");
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
