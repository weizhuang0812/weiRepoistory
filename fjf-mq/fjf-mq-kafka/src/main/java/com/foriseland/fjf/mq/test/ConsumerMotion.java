package com.foriseland.fjf.mq.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foriseland.fjf.mq.consumer.ProxyKafkaConsumer;
import com.foriseland.fjf.mq.exceptions.KafkaBaseException;
import com.foriseland.fjf.mq.motion.IKafkaConsumerServer;
import com.foriseland.fjf.mq.motion.KafkaConsumerManager;

//@Service
public class ConsumerMotion implements IKafkaConsumerServer{
	@Autowired
	private KafkaConsumerManager kafkaConsumerManager;

	@Override
	public void run() {
		System.out.println("进入代理..............");
		String topic = "mykafka696-1-2-1";
		String groupId = "groupId";
		this.execute(topic, groupId);
	}
	
	@Override
	public boolean execute(String topic, String groupId) throws KafkaBaseException {
		System.out.println("kafkaConsumerManager-----------------:" + kafkaConsumerManager);
		ProxyKafkaConsumer<String, String> kafkaConsumer = kafkaConsumerManager.getKafkaConsumer(topic, groupId); // "groupId1"
		// kafkaConsumer.subscribe(Arrays.asList(topic));
		// kafkaConsumer.seekToBeginning(new ArrayList<TopicPartition>());
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.proxyMq().poll(1000);
				for (ConsumerRecord<String, String> record : records) {
					System.out.println("fetched from partition " + record.partition() + ", offset: " + record.offset()
							+ ", message: " + record.value());
				}
				System.out.println("OrderConsumerMotion -####[size]:" + records.count());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (kafkaConsumer != null) {
				kafkaConsumer.proxyMq().close();
				System.out.println("跪安");
			}
		}
		return true;
	}

	@Override
	public boolean execute(String topic, long offset) throws KafkaBaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String topic, long offset, int partition) throws KafkaBaseException {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public boolean execute(String topic) throws KafkaBaseException {
		// TODO Auto-generated method stub
		return false;
	}

	

}
