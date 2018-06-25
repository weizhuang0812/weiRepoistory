package producer.demo;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.RecordMetadata;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SimpleKafkaProducer {

	public static void main(String[] args) {
		Random rnd = new Random();

//		Properties props = new Properties();
//		// 配置kafka集群的broker地址，建议配置两个以上，以免其中一个失效，但不需要配全，集群会自动查找leader节点。
////		props.put("metadata.broker.list", "192.168.137.176:9092,192.168.137.176:9093");
//		props.put("metadata.broker.list", "192.168.2.91:9092");
//		// 配置value的序列化类
//		// key的序列化类key.serializer.class可以单独配置，默认使用value的序列化类
//		props.put("serializer.class", "kafka.serializer.StringEncoder");
//		// 配置partitionner选择策略，可选配置
////		props.put("partitioner.class", "cn.ljh.kafka.kafka_helloworld.SimplePartitioner");
//		props.put("request.required.acks", "1");
		
		Properties props = new Properties();
	      props.put("metadata.broker.list", "192.168.2.5:9092");
	      props.put("acks", "all");
	      props.put("retries", 0);
	      props.put("batch.size", 16384);
	      props.put("linger.ms", 1);
	      props.put("buffer.memory", 33554432);
	      props.setProperty("serializer.class","kafka.serializer.StringEncoder");
	      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		

		ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);

		KeyedMessage<String, String> data = new KeyedMessage<String, String>("mykafka696", "mykafka696", "www.example.com222");
		try {
			producer.send(data);
		} catch (Exception e) {
			 e.printStackTrace();   
		}
		producer.close();
	}
}
