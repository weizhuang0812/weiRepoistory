package producer.demo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.mq.producer.KafkaProducerGeneric;

/**
 * @ClassName: producer.demo
 * @Description:
 * @author: wangHaiyang
 * @date 2016/9/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
public class TestProducer {

    @Autowired 
    private KafkaProducerGeneric producerServer;


    @Test
    public void test() throws InterruptedException, ExecutionException {
        /*while (true) {
            Thread.sleep(8000);
            long timeTemp = System.nanoTime();
            producerServer.sendMessageRequiredCallback(timeTemp % 2 == 0 ? "A-1" : "A-2", timeTemp + "", timeTemp + "");
        }*/
    	try {
    		for(int i=0;i<3;i++) {
    			if(i%2==1) {
    				Future<RecordMetadata> future = producerServer.sendMessage("mykafka696-1-2-2", "mykafka696-1", "mykafka696-1-"+i);
    			}else {
    				Future<RecordMetadata> future = producerServer.sendMessage("mykafka696-1-2-2", "mykafka696-2", "mykafka696-2-"+i);
    			}
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
		int d = 3;
		System.out.println(d%2);
	}
}