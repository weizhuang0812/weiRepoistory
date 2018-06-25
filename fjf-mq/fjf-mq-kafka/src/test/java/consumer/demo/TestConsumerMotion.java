package consumer.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.mq.test.ConsumerMotion;

/**
 * @ClassName: producer.demo
 * @Description:
 * @author: wangHaiyang
 * @date 2016/9/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
public class TestConsumerMotion {
	
	@Autowired
	private ConsumerMotion consumer ;

    @Test
    public void test() throws Exception {
    	
    	String topic = "mykafka696-1-2-1";
    	int offset = 0;
    	int partition = 0;
    	try {
    		consumer.execute(topic,"groupId");
    		
//    		consumer.execute(topic, offset, partition);
		} catch (Exception e) {
			e.printStackTrace();
		}
    //	consumer.execute(topic);
    	System.out.println("#################");
    	String topic2 = "mykafka696-1-2-2";
    	consumer.execute(topic2,"groupId");
        System.out.println("启动 consumer !");
    }




}
