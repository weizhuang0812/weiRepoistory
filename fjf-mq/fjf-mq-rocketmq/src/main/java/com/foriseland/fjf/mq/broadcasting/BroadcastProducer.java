package com.foriseland.fjf.mq.broadcasting;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
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
public class BroadcastProducer {

	private Logger logger = Logger.getLogger(BroadcastProducer.class);
	
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private RocketProducer rocketProducer; 

	public RocketProducer getRocketProducer() {
		return rocketProducer;   
	}

	public BroadcastProducer() { 
	}

	public void shutdown() {
		rocketProducer.newInstance().shutdown();
	}

	public void setProducerGroup(String namesrvAddr,String group) throws MQClientException {
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}
		if("-".equals(namesrvAddr)){ 
		}else{ 
			rocketProducer.newInstance().setNamesrvAddr(namesrvAddr); 
		}
		if (null == group || "".equals(group)) {
			group = "broadcast";
		} 
		rocketProducer.newInstance().setProducerGroup(group);
		//rocketProducer.newInstance().start();
	} 
		
	public void start() throws MQClientException {
		 rocketProducer.newInstance().start();
	} 

	@SuppressWarnings("static-access")
	public SendResult send(String producerGroup, String topic, String tags, String keys, String body) throws UnsupportedEncodingException,MQClientException, RemotingException, MQBrokerException, InterruptedException {
		rocketProducer.newInstance().setProducerGroup(producerGroup );
		//producer.start();
		long startTime = System.currentTimeMillis();
		Message message = new Message(topic, tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
		int messageDelayTimeLevel = rocketProducer.getRocketMqClientConfig().getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		SendResult sendResult = rocketProducer.newInstance().send(message);
		long endTime = System.currentTimeMillis();
		logger.info("send cost:" + (endTime - startTime) + "ms");
		logger.info(sendResult);
		return sendResult;
	} 

	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		 
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		BroadcastProducer broadcast = context.getBean(BroadcastProducer.class);
 
		// broadcast.setProducerGroup("192.168.2.31:9876","broadcast"); 
		// broadcast.setProducerGroup("127.0.0.1:9876","broadcast"); ; 
		// broadcast.setProducerGroup("192.168.3.104:9876","broadcast"); 
		broadcast.setProducerGroup("-","broadcast"); 
		 
		for (int i = 0; i <111; i++) {
			SendResult sendResult = broadcast.send("broadcast", "broadcast", "TagA", "orderId01", "hello consumer broadcast "+ i);

			TimeUnit.MILLISECONDS.sleep(1000 * 2);
		}
		broadcast.shutdown();
	}

	public static void main1(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		// DefaultMQProducer producer = RocketProducer.newInstance();
		
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		producer = new DefaultMQProducer();

		String namesrvAddr = "192.168.2.32:9876";
		// namesrvAddr="192.168.2.31:9876;192.168.2.32:9876;192.168.2.33:9876";
		producer.setNamesrvAddr(namesrvAddr);
		producer.setProducerGroup("broadcast");
		producer.setInstanceName("broadcast");
		producer.start();
		for (int i = 0; i < 20000; i++) {
			long time = System.currentTimeMillis();
			Message message = new Message("TopicTest1", "Tag1", "orderId01",
					("hello consumer broadcast " + time).getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message);
			System.out.printf("%s%n", sendResult);
			System.out.println();
			TimeUnit.MILLISECONDS.sleep(1000 * 1);
		}
		producer.shutdown();
	}
}
