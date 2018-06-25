package com.foriseland.fas.messageQueue.service.rocketmq.connection.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketProducer;
 
//@Component
public class TestRocketProducer {
 
	@SuppressWarnings({ "resource", "unused", "static-access" })
	public static void main(String[] args){
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		  
		RocketProducer rp = context.getBean(RocketProducer.class);
		rp.getLogger().info(rp.newInstance().getInstanceName());
		rp.getLogger().info(rp.newInstance().getHeartbeatBrokerInterval());  
		rp.getLogger().info(rp.newInstance().getNamesrvAddr()); 
	}
	
	 
}
