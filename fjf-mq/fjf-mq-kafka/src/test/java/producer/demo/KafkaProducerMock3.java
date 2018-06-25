package producer.demo;


import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
public class KafkaProducerMock3 {
   
   
   public static void main(String[] args) {   
	   Properties props = new Properties();
//       props.put("metadata.broker.list", "192.168.2.5:9092");
//       props.setProperty("serializer.class","kafka.serializer.StringEncoder");   
//       props.put("request.required.acks","3");   
//       props.put("zookeeper.connect", "op1.admin.com:2181");
       	 props.put("bootstrap.servers", "192.168.2.5:9092");
	     props.put("acks", "all");
	     props.put("retries", 0);
	     props.put("batch.size", 16384);
	     props.put("linger.ms", 1);
	     props.put("buffer.memory", 33554432);
	     props.setProperty("serializer.class","kafka.serializer.StringEncoder");
	     props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	     props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
       //创建生产这对象
//       Producer<String, String> producer = new Producer<String, String>(props);
	     Producer<String, String> producer = new KafkaProducer<>(props);
       //生成消息
       ProducerRecord<String, String> data = new ProducerRecord<String, String>("mykafka696", "mykafka696", "www.example.com222334455");
//       KeyedMessage<String, String> data = new KeyedMessage<String, String>("mykafka696", "mykafka696", "www.example.com22233");
       try {   
           int i =1; 
//           while(i < 100){    
//               //发送消息
////        	   producer.send(data);
//        	  
//           }
           Future<RecordMetadata> future = producer.send(data);
           RecordMetadata  recordMetadata = future.get();
           System.out.printf("offset = %d ,partition = %d \n", recordMetadata.offset() ,recordMetadata.partition());
           
       } catch (Exception e) {   
           e.printStackTrace();   
       }   
       producer.close();   
   } 
}
