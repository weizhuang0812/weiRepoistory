package com.foriseland.fas.messageQueue.service.rocketmq.connection.test;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketMQPushConsumer;

//@Component
public class TestRocketMQPushConsumer {
 
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
	
	 
}
