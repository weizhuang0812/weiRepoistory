package com.foriseland.fas.messageQueue.service.rocketmq.connection.test;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketMqClientConfig;

//@Component
public class TestRocketMqClientConfig {

	 

	@SuppressWarnings({ "resource", "unused", "static-access" })
	public static void main(String[] args) {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
	  	RocketMqClientConfig rcc = context.getBean(RocketMqClientConfig.class);
	  	rcc.getLogger().info(rcc.newInstance().getInstanceName());
	  	rcc.getLogger().info(rcc.newInstance().getNamesrvAddr());
	}
}
