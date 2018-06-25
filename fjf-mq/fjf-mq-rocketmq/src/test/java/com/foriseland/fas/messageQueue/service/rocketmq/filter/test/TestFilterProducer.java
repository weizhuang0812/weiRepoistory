package com.foriseland.fas.messageQueue.service.rocketmq.filter.test;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.filter.FilterProducer;

@Component
public class TestFilterProducer {
 
	
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		FilterProducer filterProducer = context.getBean(FilterProducer.class); 
		filterProducer.setProducerGroup("-","filte_producer");
		filterProducer.getRocketProducer().newInstance().setInstanceName("filte_producer");
	 
		for (int i = 0; i < 10000000; i++) {
			Message message = new Message("filter", "TagA", ("hello file - " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			message.putUserProperty("a", String.valueOf(i));
			
			SendResult sendResult = filterProducer.send(message);
			filterProducer.getLogger().info(sendResult);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		filterProducer.shutdown();
	}

}
