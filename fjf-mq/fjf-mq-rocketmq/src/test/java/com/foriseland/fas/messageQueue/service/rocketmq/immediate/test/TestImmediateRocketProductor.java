package com.foriseland.fas.messageQueue.service.rocketmq.immediate.test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.immediate.ImmediateRocketProductor;

@Component
public class TestImmediateRocketProductor {
 
		
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
		int i=Integer.MIN_VALUE;
		while(true) {
			try {

				Message msg = new Message("TopicTest-zhangyan", // topic
						"Tag1", // tag
						"OrderID001", // key
						(i++ + " - Hello MetaQ zhangyan1").getBytes());// body
				SendResult sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

				msg = new Message("TopicTest-zhangyan", // topic
						"Tag2", // tag
						"OrderID002", // key
						(i + " - Hello MetaQ zhangyan2").getBytes());// body
				sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

				msg = new Message("TopicTest-zhangyan", // topic
						"Tag3", // tag
						"OrderID003", // key
						(i + " - Hello MetaQ zhangyan3").getBytes());// body
				sendResult = immediateRocketProductor.send(msg);
				System.out.println(sendResult);

			} catch (Exception e) {
				e.printStackTrace();
			}

			TimeUnit.SECONDS.sleep( 2);
			
			if(i == Integer.MAX_VALUE){
				break;
			}
		}
		immediateRocketProductor.shutdown();
	}
}
