package consumer.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class TestConsumer {
	private static KafkaConsumer<String, String> kafkaConsumer;

	public static KafkaConsumer<String, String> getConsumer() {
		if (kafkaConsumer == null) {
			Properties props = new Properties();

			props.put("bootstrap.servers", "192.168.2.5:9092,192.168.2.5:9093,192.168.2.5:9094");
//			props.put("bootstrap.servers", "192.168.2.5:9092");
			props.put("group.id", "1");
			props.put("enable.auto.commit", "true");
			props.put("auto.commit.interval.ms", "1000");
			props.put("session.timeout.ms", "30000");
			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
			kafkaConsumer = new KafkaConsumer<String, String>(props);
		}

		return kafkaConsumer;
	}
	
	public static void main(String[] args) {
		KafkaConsumer<String, String> consumer = getConsumer();    
        consumer.subscribe(Arrays.asList("mykafka696"));    
        consumer.seekToBeginning(new ArrayList<TopicPartition>());  
          
        while(true) {    
            ConsumerRecords<String, String> records = consumer.poll(1000);    
            for(ConsumerRecord<String, String> record : records) {
                System.out.println("fetched from partition " + record.partition() + ", offset: " + record.offset() + ", message: " + record.value());    
            }
            //按分区读取数据  
//          for (TopicPartition partition : records.partitions()) {  
//              List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);  
//              for (ConsumerRecord<String, String> record : partitionRecords) {  
//                  System.out.println(record.offset() + ": " + record.value());  
//              }  
//          }  
              
        }   
	}
	
	

}
