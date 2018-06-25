package producer.demo;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

public class KafkaProducerMock2 {
   
   public static void main(String[] args) throws Exception {
      String[] persons = {"tom","tommy","jerry","mike","lucy"};//人名
      String[] brand ={"苹果","三星","htc","华为","小米"};//品牌
      String[] product ={"手机","笔记本","台式机","平板电脑","手环"};//产品
      
      Properties props = new Properties();
      props.put("bootstrap.servers", "192.168.2.5:9092");
      props.put("acks", "all");
      props.put("retries", 0);
      props.put("batch.size", 16384);
      props.put("linger.ms", 1);
      props.put("buffer.memory", 33554432);
      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
   
      Producer<String, String> producer = new KafkaProducer<>(props);
   
      String topics = "test-ak";
      Random random = new Random();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     
      ProducerRecord<String, String> msg = new ProducerRecord<String, String>(
              "IOLO",
              persons[random.nextInt(persons.length)]+" "+
                      brand[random.nextInt(brand.length)]+" "+
                      product[random.nextInt(product.length)]
      );//tom 苹果 手机
      Future<RecordMetadata> future = producer.send(msg);
      RecordMetadata  recordMetadata = future.get();
      System.out.printf("offset = %d ,partition = %d \n", recordMetadata.offset() ,recordMetadata.partition());
   }
   
   public static String getRandomTime(Random random,Calendar can,SimpleDateFormat sdf){
      can.set(2018,random.nextInt(12)+1,random.nextInt(30));
      return sdf.format(can.getTime());
   }
}
