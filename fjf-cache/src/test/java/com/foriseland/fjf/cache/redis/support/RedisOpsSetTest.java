package com.foriseland.fjf.cache.redis.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.cache.support.RedisOpsSet;

import redis.clients.jedis.Tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-redis-test.xml"})
public class RedisOpsSetTest {
	@Autowired
	public RedisOpsSet redisOpsSet;
	
//	@Before
//	public void init(){
//		if(!redisOpsSet.exists("testSortedSet")){
//			Map<String, Double> scoreMembers = new HashMap<String, Double>();
//			scoreMembers.put("小花", 60.0);
//			scoreMembers.put("小蓝", 70.0);
//			scoreMembers.put("小黄", 80.0);
//			scoreMembers.put("小黑", 60.0);
//			redisOpsSet.zadd("testSortedSet", scoreMembers );
//			Map<String, Double> scoreMembers1 = new HashMap<String, Double>();
//			scoreMembers1.put("a", 60.0);
//			scoreMembers1.put("b", 70.0);
//			redisOpsSet.zadd("testSortedSet1", scoreMembers1 );
//		}
//	}
	
	@Test
	public void testZadd() {
		redisOpsSet.zadd("testSortedSet", 90, "小红");
		System.out.println("排名："+redisOpsSet.zrange("testSortedSet", 0, -1));
	}

	@Test
	public void testZrange() {
		Set<Tuple> zrangeWithScores = redisOpsSet.zrangeWithScores("testSortedSet1", 0, -1);
		for (Tuple tuple : zrangeWithScores) {
			System.out.println(tuple.getElement()+":"+tuple.getScore());
		}
	}

	@Test
	public void testZcard() {
		System.out.println("有序集合元素个数："+redisOpsSet.zcard("testSortedSet"));
	}
	
	@Test
	public void testZincrby(){
		Double zincrby = redisOpsSet.zincrby("testSortedSet", 25, "小黑");
		Double newValue = redisOpsSet.zscore("testSortedSet", "小黑");
		System.out.println(newValue);
	}

	@Test
	public void testZunionstore(){
		System.out.println("排名："+redisOpsSet.zrange("testSortedSet", 0, -1));
		System.out.println("排名1："+redisOpsSet.zrange("testSortedSet1", 0, -1));
		//Long zunionstore = redisOpsSet.zunionstore("testSortedSet", "testSortedSet","testSortedSet1");
		//System.out.println(zunionstore);
		//System.out.println("排名："+redisOpsSet.zrange("testSortedSet", 0, -1));
	}
	
	@Test
	public void sadd() {
		String key1 = "engine-MallMembersOfFriendsd-10001-{XXXX}";
		String key2 = "engine-MallMembersOfFriendsd-10002-{XXXX}";
		redisOpsSet.sadd(key1, "1","2","3","4","5");
		redisOpsSet.sadd(key2, "1","2","5");
	}
	
	@Test
	public void testsinter() {
		String key1 = "engine-MallMembersOfFriendsd-10001-{XXXX}";
		String key2 = "engine-MallMembersOfFriendsd-10002-{XXXX}";
		Set<String> res = redisOpsSet.sinter(key1,key2);
		System.out.println("res："+res);
		
		Set<String> smember1 = redisOpsSet.smembers(key1);
		System.out.println(smember1);
	}
	
	@Test
	public void testsinter2() {
		String key1 = "engine-MallMembersOfFriendsd-20001-{XXXX}";
		String key2 = "engine-MallMembersOfFriendsd-20002-{XXXX}";
		Long sadd = redisOpsSet.sadd(key1, "1","2","3","4","5");
		Long sadd2 = redisOpsSet.sadd(key2, "1","2","5");
		
		Set<String> res = redisOpsSet.sinter(key1,key2);
	
		Iterator<String> it = res.iterator();
		while(it.hasNext()) {
			String str = it.next();
			System.out.println(str);
		}
	}
}
