package com.foriseland.fjf.mq.motion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.foriseland.fjf.mq.consumer.ProxyKafkaConsumer;

public class KafkaConsumerManager implements IKafkaConsumerConfig{
	
	private Properties properties;
	
//	private KafkaConsumer<String, String> kafkaConsumer;
	public void init() {
		if (properties == null || properties.size() == 0) {
			throw new RuntimeException("KafkaConsumer 配置为 null,初始化配置异常,请检查KafkaConsumerManager Bean是否被初始化!");
		}
		System.out.println("[KafkaConsumer init config] -> success");
		//kafkaConsumer = new KafkaConsumer<String, String>(properties);
	}
	
//	@PreDestroy
//	public void close() {
//		if(kafkaConsumer!=null) {
//			kafkaConsumer.close();
//		}
//		kafkaConsumer = null;
//		System.err.println("[kafkaConsumer]->已关闭,资源被释放");
//	}
	
	public void close() {
		
	}
	
	/**
	 * @title: getKafkaConsumer 
	 * @describe: 定制单个Topic
	 * @param topic
	 * @return KafkaConsumer<String, String>
	 * @author: wangHaiyang 
	 * @createTime 2018年3月5日 上午11:41:10 
	 * @throws
	 */
	public ProxyKafkaConsumer<String, String> getKafkaConsumer(String... topic){
		
		KafkaConsumer<String, String> kafkaConsumer = null;
		if(kafkaConsumer == null) {
			kafkaConsumer = new KafkaConsumer<String, String>(properties);
		}
		kafkaConsumer.subscribe(Arrays.asList(topic));
		kafkaConsumer.seekToBeginning(new ArrayList<TopicPartition>());  
		ProxyKafkaConsumer<String, String> factoryProxy  =new ProxyKafkaConsumer<>();
		factoryProxy.apacheKafkaConsumer(kafkaConsumer);
		return factoryProxy;
	}
	
	
	public ProxyKafkaConsumer<String, String> getKafkaConsumer(String topic,String groupId){
		KafkaConsumer<String, String> kafkaConsumer = null;
		if(kafkaConsumer == null) {
			if(StringUtils.isEmpty(groupId)) {
				groupId = "1";
			}
			properties.put("group.id", groupId);
			kafkaConsumer = new KafkaConsumer<String, String>(properties);
		}
		kafkaConsumer.subscribe(Arrays.asList(topic));
		kafkaConsumer.seekToBeginning(new ArrayList<TopicPartition>());  
		ProxyKafkaConsumer<String, String> factoryProxy  =new ProxyKafkaConsumer<>();
		factoryProxy.apacheKafkaConsumer(kafkaConsumer);
		return factoryProxy;
	}
	
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Properties getProperties() {
		return this.properties;
	}
}