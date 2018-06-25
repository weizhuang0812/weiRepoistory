package com.foriseland.fjf.mq.immediate;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.mq.connection.RocketMQPushConsumer;

@Component
public class ImmediateRocketConsumer {

	private Logger logger = Logger.getLogger(ImmediateRocketConsumer.class);
		
	@Autowired
	private RocketMQPushConsumer rocketMQPushConsumer;
	
	private String consumerGroup ; 

	public ImmediateRocketConsumer() {
	}
	
	public ImmediateRocketConsumer(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public RocketMQPushConsumer getRocketMQPushConsumer() {
		return rocketMQPushConsumer;
	}
	
	public Logger getLogger() {
		return logger;
	}
 
	public void setConsumerGroup(String namesrvAddr, String group) throws MQClientException {
		if (null == namesrvAddr || "".equals(namesrvAddr)) {
			throw new MQClientException(0, "namesrvAddr can not empty!");
		}

		if ("-".equals(namesrvAddr)) {
		} else {
			rocketMQPushConsumer.newInstance().setNamesrvAddr(namesrvAddr);
		}

		if ((null == group || "".equals(group))&&(null == this.consumerGroup || "".equals(this.consumerGroup))) {
			this.consumerGroup = "immediateRocketConsumer";
			group = "immediateRocketConsumer";
		}
		
		if((null != group && !"".equals(group))&&(null == this.consumerGroup || "".equals(this.consumerGroup))){
			this.consumerGroup = group;
		}		
		rocketMQPushConsumer.newInstance().setConsumerGroup(this.consumerGroup);
	}

	public void subscribe(String topic, String subExpression) throws MQClientException{
		rocketMQPushConsumer.newInstance().subscribe(topic, subExpression);
	}
	

	public void registerMessageListener(MessageListenerConcurrently lister) throws MQClientException {
		rocketMQPushConsumer.newInstance().registerMessageListener(lister);
		rocketMQPushConsumer.newInstance().start();
	}

	public void start() throws MQClientException {
		rocketMQPushConsumer.newInstance().start();
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws MQClientException {
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		PropertyConfigurator.configure("E:\\Users\\pc\\workspace\\fas-messageQueue-service\\src\\main\\resources\\config\\log4j.properties"); 
		
		ImmediateRocketConsumer immediateRocketConsumer = context.getBean(ImmediateRocketConsumer.class);

		// immediateRocketConsumer.setConsumerGroup("192.168.2.31:9876","broadcast");
		immediateRocketConsumer.setConsumerGroup("-", "immediateRocketConsumer");
		immediateRocketConsumer.subscribe("TopicTest1", "");
		int consumeMessageBatchMaxSize = immediateRocketConsumer.rocketMQPushConsumer.getRocketMqClientConfig().getRocketConfig().getConsumeMessageBatchMaxSize();
		immediateRocketConsumer.rocketMQPushConsumer.newInstance().setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
		
		MessageListenerConcurrently lister = new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				immediateRocketConsumer.logger.info(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());

				MessageExt msg = msgs.get(0);
				immediateRocketConsumer.logger.info( msg.getTopic() );
				immediateRocketConsumer.logger.info(	msg.getReconsumeTimes() );
			
				try {

					if (msg.getTopic().equals("TopicTest1")) {
						// 执行TopicTest1的消费逻辑
						if (msg.getTags() != null && msg.getTags().equals("Tag1")) {
							// 执行Tag1的消费
							immediateRocketConsumer.logger.info("TopicTest1:==Tag1===");
							immediateRocketConsumer.logger.info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag2")) {
							// 执行Tag1的消费
							immediateRocketConsumer.logger.info("TopicTest1:==Tag2===");
							immediateRocketConsumer.logger.info(new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("Tag3")) {
							// 执行Tag1的消费
							immediateRocketConsumer.logger.info("TopicTest1:==Tag3===");
							immediateRocketConsumer.logger.info(new String(msg.getBody()));
						} else {
						}
						
					} else if (msg.getTopic().equals("TopicTest2")) {
						if (msg.getTags() != null && msg.getTags().equals("Tag2")) {
							// 执行Tag2的消费
							immediateRocketConsumer.logger.info("TopicTest2:=====");
							immediateRocketConsumer.logger.info(new String(msg.getBody()));
						}else{
							
						}

					} else if (msg.getTopic().equals("TopicTest3")) {
						if (msg.getTags() != null && msg.getTags().equals("Tag3")) {
							// 执行Tag2的消费
							immediateRocketConsumer.logger.info("TopicTest3:=====");
							immediateRocketConsumer.logger.info(new String(msg.getBody()));
						}
					} else {

					}
				} catch (Exception e) {
					if (msg.getReconsumeTimes() == immediateRocketConsumer.rocketMQPushConsumer.getMessageDelayLevel()) {
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}else{
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
				}

				/*try {
					TimeUnit.MILLISECONDS.sleep((long) (1000 * 0.02));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

			}
		};

		/**
		 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		 */
		immediateRocketConsumer.registerMessageListener(lister);
		immediateRocketConsumer.logger.info("immediate Consumer Started.%n");
	}

}
