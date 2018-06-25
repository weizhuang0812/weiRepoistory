package com.foriseland.fjf.mq.connection;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RocketmqConfiguration { 

	private Logger logger = Logger.getLogger(RocketmqConfiguration.class);  
	
	private static String namesrvAddr;
    private static String instanceName;
    private static int clientCallbackExecutorThreads;
    private static int pollNameServerInterval;
    private static int heartbeatBrokerInteval;
    private static int persistConsumerOffsetInterval; 
	private static int checkThreadPoolMinSize;
    private static int checkThreadPoolMaxSize;
    private static int checkRequestHoldMax;
    private static int messageDelayTimeLevel;
    private static int consumeMessageBatchMaxSize;

    public RocketmqConfiguration(){
    }
	    
    public   int getConsumeMessageBatchMaxSize() {
		return consumeMessageBatchMaxSize;
	}

	public   void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize) {
		RocketmqConfiguration.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
	}

	public   int getMessageDelayTimeLevel() {
		return messageDelayTimeLevel;
	}

	public   void setMessageDelayTimeLevel(int messageDelayTimeLevel) {
		RocketmqConfiguration.messageDelayTimeLevel = messageDelayTimeLevel;
	}

	public Logger getLogger() {
		return logger;
	}
	    
    public   int getCheckRequestHoldMax() {
		return checkRequestHoldMax;
	}

	public   void setCheckRequestHoldMax(int checkRequestHoldMax) {
		RocketmqConfiguration.checkRequestHoldMax = checkRequestHoldMax;
	}

	public   int getCheckThreadPoolMinSize() {
		return checkThreadPoolMinSize;
	}

	public   void setCheckThreadPoolMinSize(int checkThreadPoolMinSize) {
		RocketmqConfiguration.checkThreadPoolMinSize = checkThreadPoolMinSize;
	}

	public   int getCheckThreadPoolMaxSize() {
		return checkThreadPoolMaxSize;
	}

	public   void setCheckThreadPoolMaxSize(int checkThreadPoolMaxSize) {
		RocketmqConfiguration.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
	}  
    public   String getNamesrvAddr() {
		return namesrvAddr;
	} 

	public   void setNamesrvAddr(String namesrvAddr) {
		RocketmqConfiguration.namesrvAddr = namesrvAddr;
	} 
	
	public   String getInstanceName() {
		return instanceName;
	} 

	public   void setInstanceName(String instanceName) {
		RocketmqConfiguration.instanceName = instanceName;
	} 

	public   int getClientCallbackExecutorThreads() {
		return clientCallbackExecutorThreads;
	} 
	
	public   void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
		RocketmqConfiguration.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
	} 

	public   int getPollNameServerInterval() {
		return pollNameServerInterval;
	} 
	
	public   void setPollNameServerInterval(int pollNameServerInterval) {
		RocketmqConfiguration.pollNameServerInterval = pollNameServerInterval;
	} 
	                  
	public   int getHeartbeatBrokerInteval() {
		return heartbeatBrokerInteval;
	}
	
	public   void setHeartbeatBrokerInteval(int heartbeatBrokerInteval) {
		RocketmqConfiguration.heartbeatBrokerInteval = heartbeatBrokerInteval;
	}

	public   int getPersistConsumerOffsetInterval() {
		return persistConsumerOffsetInterval;
	}

	public   void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
		RocketmqConfiguration.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
	}
	
	

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
