package com.foriseland.fjf.mq.connection;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class RocketMqClientConfig {

	private Logger logger = Logger.getLogger(RocketMqClientConfig.class);
	
	@Autowired
	private RocketmqConfiguration rocketConfig; 

	private ClientConfig clientConfig; 

	public  RocketMqClientConfig() {
		super();
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public RocketmqConfiguration getRocketConfig() {
		return rocketConfig;
	}

	public void setRocketConfig(RocketmqConfiguration rocketConfig) {
		this.rocketConfig = rocketConfig;
	}

	public ClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}	

	public ClientConfig newInstance() {
		
		if (null == clientConfig) {
			clientConfig = new ClientConfig();
			// 客户端本机 IP 地址，某些机器会发生无法识别客户端IP地址情况，需要应用在代码中强制指定
			// clientConfig.setClientIP("192.168.28.94");
			// Name Server 地址列表，多个 NameServer 地址用分号 隔开
			clientConfig.setNamesrvAddr(rocketConfig.getNamesrvAddr());
			// 客户端实例名称，客户端创建的多个 Producer、 Consumer 实际是共用一个内部实例（这个实例包含
			// 网络连接、线程资源等）,默认值:DEFAULT
			clientConfig.setInstanceName(rocketConfig.getInstanceName());
			// 通信层异步回调线程数,默认值4
			clientConfig.setClientCallbackExecutorThreads(rocketConfig.getClientCallbackExecutorThreads());
			// 轮询 Name Server 间隔时间，单位毫秒,默认：30000
			clientConfig.setPollNameServerInterval(rocketConfig.getPollNameServerInterval());
			// 向 Broker 发送心跳间隔时间，单位毫秒,默认：30000
			clientConfig.setHeartbeatBrokerInterval(rocketConfig.getHeartbeatBrokerInteval());
			// 持久化 Consumer 消费进度间隔时间，单位毫秒,默认：5000
			clientConfig.setPersistConsumerOffsetInterval(rocketConfig.getPersistConsumerOffsetInterval());  
		}
		return clientConfig;
	}

	@Override
	public String toString() {
		return clientConfig.toString();
	}

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
