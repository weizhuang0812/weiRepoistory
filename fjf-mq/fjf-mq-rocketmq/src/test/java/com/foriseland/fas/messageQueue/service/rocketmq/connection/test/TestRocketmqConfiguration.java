package com.foriseland.fas.messageQueue.service.rocketmq.connection.test;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketmqConfiguration;

//@Component
public class TestRocketmqConfiguration { 
 

	@SuppressWarnings({ "resource", "unused", "static-access" })
	public static void main(String[] args){
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		 
		RocketmqConfiguration rc = context.getBean(RocketmqConfiguration.class);
		rc.getLogger().info(rc.getInstanceName());
		rc.getLogger().info(rc.getClientCallbackExecutorThreads());
		rc.getLogger().info(rc.getInstanceName());
		rc.getLogger().info(rc.getNamesrvAddr());
		rc.getLogger().info(rc.getMessageDelayTimeLevel());
		rc.getLogger().info(rc.getConsumeMessageBatchMaxSize());
		rc.getLogger().info(rc.getConsumeMessageBatchMaxSize());
		
	}
}
