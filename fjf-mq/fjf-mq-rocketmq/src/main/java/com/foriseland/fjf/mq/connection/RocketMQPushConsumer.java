package com.foriseland.fjf.mq.connection;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RocketMQPushConsumer {

	private Logger logger = Logger.getLogger(RocketMQPushConsumer.class);  
	
	private static DefaultMQPushConsumer consumer;
	
	@Autowired
	private RocketMqClientConfig rocketMqClientConfig;
	
	public Logger getLogger() {
		return logger;
	}
	
	public RocketMqClientConfig getRocketMqClientConfig() {
		return rocketMqClientConfig;
	}

	public void setRocketMqClientConfig(RocketMqClientConfig rocketMqClientConfig) {
		this.rocketMqClientConfig = rocketMqClientConfig;
	}

	private RocketMQPushConsumer() {
		super();
		if(null == consumer){
			consumer = new DefaultMQPushConsumer();
		}
	}
	
	public int getMessageDelayLevel(){
		return rocketMqClientConfig.getRocketConfig().getMessageDelayTimeLevel();
	}

	private static DefaultMQPushConsumer restConfig(ClientConfig config) {
		consumer.resetClientConfig(config);
		return consumer;
	}

	public DefaultMQPushConsumer newInstance() {
		consumer.resetClientConfig(rocketMqClientConfig.newInstance());
		return consumer;
	} 

	@SuppressWarnings({ "resource", "unused", "static-access" })
	public static void main(String[] args) {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		RocketMQPushConsumer rpc = context.getBean(RocketMQPushConsumer.class);
		rpc.getLogger().info(rpc.newInstance().getInstanceName());
		rpc.getLogger().info(rpc.newInstance().getHeartbeatBrokerInterval());
		rpc.getLogger().info(rpc.newInstance().getNamesrvAddr());

	}
	
	public void setSubscribeParam(String topic, String subExpression) throws MQClientException { 
		consumer.subscribe(topic, subExpression);
	} 
	
	public Map<String, String> getSubscribeList(){
		return consumer.getSubscription();
	}
	
	public void setSubscribeList(Map<String, String> subscription){
		consumer.setSubscription(subscription);
	}
	
	public void deleteSubscribeAndTopic(String topic){
		Map<String, String> subscription = consumer.getSubscription();
		subscription.remove(topic);
		consumer.setSubscription(subscription); 
	}
	 
	public void registerMessageListener(MessageListenerConcurrently lister) throws MQClientException {
		consumer.registerMessageListener(lister);
		consumer.start();
	}
	
	public void setMessageModelBroadcasting() { 
		consumer.setMessageModel(MessageModel.BROADCASTING);
	}
	
	public void setMessageModelClustering() { 
		consumer.setMessageModel(MessageModel.CLUSTERING);
	}
}
