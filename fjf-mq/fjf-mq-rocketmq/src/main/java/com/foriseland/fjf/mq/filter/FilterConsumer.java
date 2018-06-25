package com.foriseland.fjf.mq.filter;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketMQPushConsumer;

@Component
public class FilterConsumer {
	
	private Logger logger = Logger.getLogger(FilterConsumer.class);
		
	@Autowired
	private RocketMQPushConsumer rocketMQPushConsumer;
	 
	public FilterConsumer(){		 
	}

	public RocketMQPushConsumer getRocketMQPushConsumer() {
		return rocketMQPushConsumer;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public void setConsumerGroup(String namesrvAddr, String consumerGroup) throws MQClientException{
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}
		
		if("-".equals(namesrvAddr)){ 
		}else{ 
			rocketMQPushConsumer.newInstance().setNamesrvAddr(namesrvAddr); 
		}
		
		if(null == consumerGroup || "".equals(consumerGroup)){
			consumerGroup = "filter_consumer";
		}
		rocketMQPushConsumer.newInstance().setConsumerGroup(consumerGroup);
	}
	
	
	public void subscribe(String topic, MessageSelector select) throws MQClientException{
		rocketMQPushConsumer.newInstance().subscribe(topic, select);
	}
	
	public void subscribe(String topic, String fullClassName, String filterClassSource) throws MQClientException{
		rocketMQPushConsumer.newInstance().subscribe(topic, fullClassName, filterClassSource);
	}
	
	public void registerMessageListener(MessageListenerConcurrently listener) throws MQClientException{
		rocketMQPushConsumer.newInstance().registerMessageListener(listener);
		rocketMQPushConsumer.newInstance().start();
	}
	
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
		String instanceName = consumer.rocketMQPushConsumer.newInstance().getInstanceName();
		consumer.rocketMQPushConsumer.newInstance().setInstanceName("filter_consumer");
		instanceName = consumer.rocketMQPushConsumer.newInstance().getInstanceName();
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
						 consumer.logger.info("filter Receive message[msgId=" + msg.getMsgId() + "] " + (System.currentTimeMillis() - msg.getStoreTimestamp()) + "ms filter");
						 consumer.logger.info(msg.getUserProperty("a"));
                     
					} catch (Exception e) {
						if (msg.getReconsumeTimes() == consumer.rocketMQPushConsumer.getMessageDelayLevel()) {
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
		consumer.logger.info("Filter ConsumerB Started.%n");
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
