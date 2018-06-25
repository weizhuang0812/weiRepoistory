package com.foriseland.fjf.cache.redis.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.cache.support.RedisOpsMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-redis-test.xml"})
public class RedisOpsMapTest {
	@Autowired
	public RedisOpsMap redisOpsMap;
	
	@Before
	public void init(){
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put("name", "张三");
		fieldValues.put("age", "20");
		redisOpsMap.hmset("hash-map", fieldValues );;
	}
	
	@Test
	public void testHset() {
		redisOpsMap.hset("hash-map", "testHash", "map");
		System.out.println("hash设置的值："+redisOpsMap.hget("hash-map", "testHash"));
	}
	
	@Test
	public void testHget(){
		System.out.println("hash设置的值："+redisOpsMap.hget("hash-map", "testHash"));
	}
	
	@Test
	public void testHexists(){
		System.out.println("判断是否存在："+redisOpsMap.hexists("hash-map", "testHash"));
	}
	
	@Test
	public void testHgetAll(){
		Map<String, String> hgetAll = redisOpsMap.hgetAll("hash-map");
		Set<Entry<String,String>> entrySet = hgetAll.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println("获取所有的值："+entry.getKey()+":"+entry.getValue());
		}
	}
	
	@Test
	public void testHmget(){
		List<String> hmget = redisOpsMap.hmget("hash-map", "name",  "age", "addr");
		System.out.println("获取指定的值："+hmget);
	}
	
	@Test
	public void testHlen(){
		System.out.println("hash-map长度:"+redisOpsMap.hlen("hash-map"));
	}
	
	@Test
	public void testHkeys(){
		Set<String> hfields = redisOpsMap.hkeys("hash-map");
		System.out.println("获取hash-map中fields:"+hfields);
	}
	
	@Test
	public void testHvals(){
		List<String> hvals = redisOpsMap.hvals("hash-map");
		System.out.println("获取hash-map中values:"+hvals);
	}
	
	@Test
	public void testHdel(){
		String field1 = "testHash";
		String field2 = "name";
		String field3 = "addr";
		long count = redisOpsMap.hdel("hash-map", field1, field2, field3);
		System.out.println("删除"+count);
	}
}
