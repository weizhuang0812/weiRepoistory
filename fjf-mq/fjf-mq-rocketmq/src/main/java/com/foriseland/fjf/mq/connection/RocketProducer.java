package com.foriseland.fjf.mq.connection;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.TopicPublishInfo;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
 
@Component
public class RocketProducer {

	private Logger logger =  LogManager.getLogger(RocketProducer.class);
	
	@Autowired
	private  RocketMqClientConfig rocketMqClientConfig; 

	private DefaultMQProducer producer;
	
	public RocketMqClientConfig getRocketMqClientConfig() {
		return rocketMqClientConfig;
	}   

	public void setRocketMqClientConfig(RocketMqClientConfig rocketMqClientConfig) {
		this.rocketMqClientConfig = rocketMqClientConfig;
	}

	private RocketProducer() {
		super();
	}
	
	public Logger getLogger() {
		return logger;
	}
	

	
	public DefaultMQProducer newInstance(){
		if(null == producer){
			producer = new DefaultMQProducer();
			producer.resetClientConfig(rocketMqClientConfig.newInstance()); 
			System.out.println(rocketMqClientConfig.newInstance().getInstanceName());
			producer.setInstanceName(rocketMqClientConfig.newInstance().getInstanceName());
			//在发送消息时，自动创建服务器不存在的topic，默认创建的队列数 默认值 4
			producer.setDefaultTopicQueueNums(4);
			//发送消息超时时间，单位毫秒 : 默认值 10000
			producer.setSendMsgTimeout(10000);
			// 消息Body超过多大开始压缩（Consumer收到消息会自动解压缩）,单位字节 默认值 4096
			producer.setCompressMsgBodyOverHowmuch(4096);
			//如果发送消息返回sendResult，但是sendStatus!=SEND_OK,是否重试发送 默认值 FALSE
			producer.setRetryAnotherBrokerWhenNotStoreOK(false); 
		}
		return producer;
	}
	
	private DefaultMQProducer restConfig(ClientConfig config) {
		producer.resetClientConfig(config);
		return producer;
	}	 

	public DefaultMQProducer restConfigInstance(ClientConfig config) {
		producer.resetClientConfig(config);
		return producer;
	}
	 
	@SuppressWarnings({ "resource", "unused", "static-access" })
	public static void main(String[] args){
		String xmlPath = "classpath:config/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath); 
		  
		RocketProducer rp = context.getBean(RocketProducer.class);
		rp.getLogger().info(rp.newInstance().getInstanceName());
		rp.getLogger().info(rp.newInstance().getHeartbeatBrokerInterval());  
		rp.getLogger().info(rp.newInstance().getNamesrvAddr()); 
	}
	
	public void creatTopic(String key, String newTopic) throws MQClientException{
		int queueNum = 16 ;
		producer.createTopic(key, newTopic, queueNum);
		// producer.createTopic(key, newTopic, queueNum, topicSysFlag); 
	}
	
	public String getTopicKey(){
		return producer.getCreateTopicKey();
	}
	
	public void setTopic(String createTopicKey){
		producer.setCreateTopicKey(createTopicKey); 
		producer.setRetryTimesWhenSendFailed(3); //失败的情况重发3次
	}

	public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed){
		if(retryTimesWhenSendFailed>0)
			producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed); //失败的情况重发3次
	}
 
	public SendResult send( String tags, String keys, String body) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
		// producer.setProducerGroup(producer.getProducerGroup());  
		Message message = new Message(producer.getCreateTopicKey(), tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
		int messageDelayTimeLevel = rocketMqClientConfig.getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		SendResult sendResult = producer.send(message,1000); 
		return sendResult;
	}
	
	public ArrayList<SendResult> sendBatch( String tags, String keys, ArrayList<String> bodys) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
		// producer.setProducerGroup(producer.getProducerGroup());  
		int count = 0;
		ArrayList<SendResult> results = new ArrayList<SendResult>(bodys.size());
		for(String body : bodys){
			Message message = new Message(producer.getCreateTopicKey(), tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
			int messageDelayTimeLevel = rocketMqClientConfig.getRocketConfig().getMessageDelayTimeLevel();
			message.setDelayTimeLevel(messageDelayTimeLevel);
			SendResult sendResult = producer.send(message,1000); 
			results.add(sendResult);
			count++;
		}
		return results;
	}
	
	public SendResult send( String tags, String keys, String body,long millisSeconds) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
		// producer.setProducerGroup(producer.getProducerGroup());  
		Message message = new Message(producer.getCreateTopicKey(), tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
		int messageDelayTimeLevel = rocketMqClientConfig.getRocketConfig().getMessageDelayTimeLevel();
		message.setDelayTimeLevel(messageDelayTimeLevel);
		SendResult sendResult = producer.send(message,millisSeconds); 
		return sendResult;
	}
	
	public ArrayList<SendResult> sendBatch( String tags, String keys, ArrayList<String> bodys,long millisSeconds) throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
		// producer.setProducerGroup(producer.getProducerGroup());  
		int count = 0;
		ArrayList<SendResult> results = new ArrayList<SendResult>(bodys.size());
		for(String body : bodys){
			Message message = new Message(producer.getCreateTopicKey(), tags, keys, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
			int messageDelayTimeLevel = rocketMqClientConfig.getRocketConfig().getMessageDelayTimeLevel();
			message.setDelayTimeLevel(messageDelayTimeLevel);
			SendResult sendResult = producer.send(message,millisSeconds); 
			results.add(sendResult);
			count++;
		}
		return results;
	}
	
	public Set<String> getPublishTopicList(){
		return producer.getDefaultMQProducerImpl().getPublishTopicList();
	}
	
	public MessageQueue selectOneMessageQueue(final TopicPublishInfo tpInfo, final String lastBrokerName){
		return producer.getDefaultMQProducerImpl().selectOneMessageQueue(tpInfo, lastBrokerName);
	}
	
	
}
