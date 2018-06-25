package com.foriseland.fjf.mq.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @className: IKafkaFactory 
 * @describe: Kafka基础类，用于切换Apache或华为云的基础类
 * @author: wangHaiyang  
 * @createTime 2018年3月24日 下午4:31:29
 */
public interface IKafkaFactory<K,V> {
	KafkaConsumer<String,String> proxyMq(); // 默认走Kafka的Consumer

//	KafkaConsumer<String, String> kafkaConsumer();
	
//	KafkaConsumer<String,String> kafkaConsumer();
	
	
}
