package com.foriseland.fjf.mq.filter;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketProducer;

@Component
public class FilterProducer {

	private Logger logger = Logger.getLogger(FilterProducer.class);
	
	@Autowired
	private RocketProducer rocketProducer; 

	public FilterProducer(){
	}
	
	public RocketProducer getRocketProducer() {
		return rocketProducer;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public void setProducerGroup(String namesrvAddr, String filteProducerGroup) throws MQClientException{
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}
		
		if("-".equals(namesrvAddr)){ 
		}else{ 
			rocketProducer.newInstance().setNamesrvAddr(namesrvAddr); 
		}
		if(null == filteProducerGroup || "".equals(filteProducerGroup)){
			filteProducerGroup = "filter";
		}
		rocketProducer.newInstance().setProducerGroup(filteProducerGroup);
		rocketProducer.newInstance().start();
	}
	
	public void shutdown(){
		rocketProducer.newInstance().shutdown();
	}
	
	public SendResult send(String topic, String tags,String body) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException{
		Message message = new Message(topic, tags,body.getBytes(RemotingHelper.DEFAULT_CHARSET));
		int messageDelayTimeLevel = rocketProducer.getRocketMqClientConfig().getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		return rocketProducer.newInstance().send(message);
	}
	
	public SendResult send(Message message) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException{
		int messageDelayTimeLevel = rocketProducer.getRocketMqClientConfig().getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		return rocketProducer.newInstance().send(message);
	} 
	
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		FilterProducer filterProducer = context.getBean(FilterProducer.class); 
		filterProducer.setProducerGroup("-","filte_producer");
		filterProducer.rocketProducer.newInstance().setInstanceName("filte_producer");
	 
		for (int i = 0; i < 10000000; i++) {
			Message message = new Message("filter", "TagA", ("hello file - " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			message.putUserProperty("a", String.valueOf(i));
			
			SendResult sendResult = filterProducer.send(message);
			filterProducer.logger.info(sendResult);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		filterProducer.shutdown();
	}

}
