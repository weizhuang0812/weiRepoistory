package com.foriseland.fjf.mq.consumer;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ApacheKafkaFactory<K, V> extends KafkaConsumer<K, V> {

	public ApacheKafkaFactory(Properties properties) {
		super(properties);
	}



}
