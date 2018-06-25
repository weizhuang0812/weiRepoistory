package com.foriseland.fjf.mq.immediate;

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
public class ImmediateRocketProductor {

	private Logger logger = Logger.getLogger(ImmediateRocketProductor.class);
	
	@Autowired
	private RocketProducer rocketProducer;
	
	private String producerGroup;
	
	public ImmediateRocketProductor() { 
	}

	public ImmediateRocketProductor(String producerGroup) {
		this.producerGroup = producerGroup;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public void shutdown() {
		rocketProducer.newInstance().shutdown();
	}

	public void setProducerGroup(String namesrvAddr, String group) throws MQClientException {
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}
		if ("-".equals(namesrvAddr)) {
		} else {
			rocketProducer.newInstance().setNamesrvAddr(namesrvAddr);
		}
		
		if((null == group || "".equals(group)) && (null == this.producerGroup || "".equals(this.producerGroup))) {
			this.producerGroup = "immediateRocketProductor";
			group = "immediateRocketProductor";
		}
		
		if((null != group && !"".equals(group))&&(null == this.producerGroup || "".equals(this.producerGroup))){
			this.producerGroup = group;
		}
				
		rocketProducer.newInstance().setProducerGroup(this.producerGroup); 
	}
	
	
	
	public SendResult send(String producerGroup, String topic, String tags, String keys, String body) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
		rocketProducer.newInstance().setProducerGroup(producerGroup);  
		Message message = new Message(topic, tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
		int messageDelayTimeLevel = rocketProducer.getRocketMqClientConfig().getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		SendResult sendResult = rocketProducer.newInstance().send(message); 
		return sendResult;
	}

	public SendResult send(Message message) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException { 
		int messageDelayTimeLevel = rocketProducer.getRocketMqClientConfig().getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		SendResult sendResult = rocketProducer.newInstance().send(message); 
		return sendResult;
	}

	public void start() throws MQClientException {
		rocketProducer.newInstance().start();  
	}
		
	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {

		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		ImmediateRocketProductor immediateRocketProductor = context.getBean(ImmediateRocketProductor.class);

		// immediateRocketProductor.setProducerGroup("192.168.2.31:9876","broadcast");
		// immediateRocketProductor.setProducerGroup("127.0.0.1:9876","broadcast");		 
		// immediateRocketProductor.setProducerGroup("192.168.3.104:9876","broadcast");
		// String namesrvAddr = "192.168.2.32:9876";
		// namesrvAddr = "192.168.3.104:9876";
		// namesrvAddr="192.168.2.31:9876;192.168.2.32:9876;192.168.2.33:9876";
		immediateRocketProductor.setProducerGroup("-", "immediateRocketProductor");

		/**
		 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		 * 注意：切记不可以在每次发送消息时，都调用start方法
		 */
		immediateRocketProductor.start();

		/**
		 * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
		 * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br>
		 * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br>
		 * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
		 */
		for (int i = 0; i < 1 ; i++) {
			try {

				Message msg = new Message("TopicTest1", // topic
						"Tag1", // tag
						"OrderID001", // key
						(i + " - Hello MetaQ zhangyan1").getBytes());// body
				SendResult sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

				msg = new Message("TopicTest1", // topic
						"Tag2", // tag
						"OrderID002", // key
						(i + " - Hello MetaQ zhangyan2").getBytes());// body
				sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

				msg = new Message("TopicTest1", // topic
						"Tag3", // tag
						"OrderID003", // key
						(i + " - Hello MetaQ zhangyan3").getBytes());// body
				sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

			} catch (Exception e) {
				e.printStackTrace();
			}

			//TimeUnit.MILLISECONDS.sleep(1000 * 2);
		}
		immediateRocketProductor.shutdown();
	}
}
