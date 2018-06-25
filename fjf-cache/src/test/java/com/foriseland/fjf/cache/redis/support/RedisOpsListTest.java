package com.foriseland.fjf.cache.redis.support;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.cache.support.RedisOpsList;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-redis-test.xml"})
public class RedisOpsListTest {
	
	@Autowired
	public RedisOpsList redisOpsList;
	
//	@Before
//	public void init(){
//		if(!redisOpsList.exists("qumao-quying-A8A30DA90EF251A586527B4EDBBBABC8"))
//			redisOpsList.lpush("testList", "1", "2");
//	}

	@Test
	public void testLpush() {
		
		redisOpsList.lpush("qumao-quying-A8A30DA90EF251A586527B4EDBBBABC8", "3", "4");
	}
	
	@Test
	public void testLrange(){
		System.out.println("获取列表的长度："+redisOpsList.llen("testList"));
		List<String> list = redisOpsList.lrange("testList", 0, -1);
		System.out.println("获取列表指定范围的集合："+list);
	}
	
	@Test
	public void testLindex(){
		String value = redisOpsList.lindex("testList", 0);
		System.out.println("获取0位置的元素："+value);
	}
	
	@Test
	public void testLpop(){
		String lpop = redisOpsList.lpop("testList");
		System.out.println("弹出的元素："+lpop);
		System.out.println("获取列表的元素集合："+redisOpsList.lrange("testList", 0, -1));
	}
	
	@Test
	public void testLset(){
		redisOpsList.lset("testList", 0, "10");
		System.out.println("获取列表的元素集合："+redisOpsList.lrange("testList", 0, -1));
	}
	
	@Test
	public void testLtrim(){
		redisOpsList.ltrim("testList", 1, -1);
		System.out.println("获取只保留指定区间内的元素："+redisOpsList.lrange("testList", 0, -1));
	}
	
	@Test
	public void testLinsert(){
		redisOpsList.linsert("testList", LIST_POSITION.BEFORE, "2", "5");
		System.out.println("获取插入后元素集合："+redisOpsList.lrange("testList", 0, -1));
	}
	
	@Test
	public void testLrem(){
		Long remCount = redisOpsList.lrem("testList", 2, "4");
		System.out.println("删除了"+remCount+"个元素");
	}

}
