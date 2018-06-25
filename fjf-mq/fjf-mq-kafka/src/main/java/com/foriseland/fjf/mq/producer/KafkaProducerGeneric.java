package com.foriseland.fjf.mq.producer;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.foriseland.fjf.mq.domain.FLKafkaProducer;
import com.foriseland.fjf.mq.domain.FLOpertionType;
import com.foriseland.fjf.mq.property.FLKafkaOtherProperties;
import com.foriseland.fjf.mq.util.KafkaUtils;

/**
 * 
 * @className: KafkaProducerGeneric 
 * @describe: KAFKA的Product服务
 * @author: wangHaiyang  
 * @createTime 2018年3月3日 下午3:57:03
 */
public class KafkaProducerGeneric {

	private Properties properties;

	private KafkaProducer<String, String> kafkaProducer;

	private int topPartitionCount;

	void init() {
		if (properties == null || properties.size() == 0) {
			throw new RuntimeException("KafkaProducerGeneric 配置为 null,初始化配置异常,请检查KafkaProducerGeneric Bean是否被初始化!");
		}
		Object tpc = properties.get(FLKafkaOtherProperties.PRODUCER_PARTITION_COUNT);
		this.topPartitionCount = tpc == null ? 3 : Integer.valueOf(tpc.toString());
		this.kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);
		System.out.println("[KafkaProducerGeneric init config] -> success");
	}

	/**
	 * 简单的生产消息,
	 *
	 * @param topic
	 * @param key
	 * @param value
	 */
	public Future<RecordMetadata> sendMessage(String topic, String key, String value) {
		KafkaUtils.ruleTopic(topic);
		return this.kafkaProducer.send(new ProducerRecord<String, String>(topic, key, value));
		
	}

	public RecordMetadata sendMessage(String topic, String key, String value, boolean isTimely) {
		KafkaUtils.ruleTopic(topic);
		Future<RecordMetadata> result = sendMessage(topic, key, value);
		return futureResult(isTimely, result);
	}

	/**
	 * 指定分区生产消息,同步生产API
	 * @param topic
	 * @param key
	 * @param value
	 * @param partition
	 */
	public Future<RecordMetadata> sendMessageByPartition(String topic, String key, String value, int partition) {
		KafkaUtils.ruleTopic(topic);
		return this.kafkaProducer.send(new ProducerRecord<String, String>(topic, partition, key, value));
	}
	
	

	/**
	 * @param topic
	 * @param key
	 * @param value
	 * @param partition
	 */
	public RecordMetadata sendMessage(String topic, String key, String value, int partition, boolean isTimely) {
		KafkaUtils.ruleTopic(topic);
		Future<RecordMetadata> result = this.sendMessageByPartition(topic, key, value, partition);
		return this.futureResult(isTimely, result);
	}

	/**
	 * 回调函数发送消息
	 *
	 * @param topic
	 * @param key
	 * @param value
	 * @param callback
	 * @return
	 */
	public Future<RecordMetadata> sendMessageAndCallback(String topic, String key, String value, Callback callback) {
		KafkaUtils.ruleTopic(topic);
		return this.kafkaProducer.send(new ProducerRecord<String, String>(topic, key, value), callback);
	}

	/**
	 * 指定分区和回调发送消息,
	 *
	 * @param topic
	 * @param key
	 * @param value
	 * @param callback
	 * @return
	 */
	public Future<RecordMetadata> sendMessageAndCallback(String topic, String key, String value, int partition,
			Callback callback) {
		KafkaUtils.ruleTopic(topic);
		return this.kafkaProducer.send(new ProducerRecord<String, String>(topic, partition, key, value), callback);
	}

	/**
	 * 指定分区和回调发送消息并且回调,
	 *
	 * @param topic
	 * @param key
	 * @param value
	 * @param
	 * @return
	 */
	public Future<RecordMetadata> sendMessageRequiredCallback(String topic, String key, String value) {
		KafkaUtils.ruleTopic(topic);
		return this.kafkaProducer.send(new ProducerRecord<String, String>(topic, key, value),
				new CallBack(topic, key, value));
	}

	RecordMetadata futureResult(boolean isTimely, Future<RecordMetadata> result) {
		if (isTimely) {
			try {
				return result.get();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public void close() {
		kafkaProducer.close();
		kafkaProducer = null;
		System.out.println("[kafkaProducer close]");
	}

	// ------------------- setter getter---------------------

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
 
	public int getTopPartitionCount() {
		return topPartitionCount;
	}

	public void setTopPartitionCount(int topPartitionCount) {
		this.topPartitionCount = topPartitionCount;
	}

	public static class CallBack implements Callback {
		private String key;
		private String value;
		@SuppressWarnings("unused")
		private String topic;

		public CallBack(String topic, String key, String value) {
			this.key = key;
			this.topic = topic;
			this.value = value;
		}

		FLKafkaProducer createKfkaProducer(Throwable exception, String topic, long offset, int partition, String key,
				String value, long checksum) {
			FLKafkaProducer producer = new FLKafkaProducer();
			Date now = new Date();
			producer.setKafkaChecksum(System.nanoTime() + "");
			producer.setExceptionMessage(exception.getMessage());
			producer.setKafkaProducerId(KafkaUtils.get32UUID());
			producer.setKafkaOffset(offset);
			producer.setOpertionType(FLOpertionType.UNRESOLVE);
			producer.setKafkaTopic(topic);
			producer.setResolveTime(now);
			producer.setUpdateTime(now);
			producer.setKafkaPartition(partition);
			producer.setKafkaKey(key);
			producer.setKafkaValue(value);
			return producer;
		}

		@Override
		public void onCompletion(RecordMetadata record, Exception e) {
			if (record != null) {
				System.err.println("record : " + record);
				if (e != null) {
					System.err.println("Exception : " + e.getMessage() + "..." + e.getLocalizedMessage());
					e.printStackTrace();
				}

				// TODO: 16/7/27
				System.err.printf("offset=%d , partition=%s , topic=%s  key=%s,  value=%s  \r\n", record.offset(),record.partition(), record.topic(), this.key, this.value);
			}
		}
	}
}
