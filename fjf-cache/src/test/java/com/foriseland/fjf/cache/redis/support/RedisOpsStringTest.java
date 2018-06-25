package com.foriseland.fjf.cache.redis.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.cache.support.RedisOpsSet;
import com.foriseland.fjf.cache.support.RedisOpsString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-redis-test.xml" })
public class RedisOpsStringTest {

	@Autowired
	public RedisOpsString redisOpsString;
	
	@Autowired
	public RedisOpsSet redisOpsSet;

	/*
	 * @Before public void init(){ if(!redisOpsString.exists("testStr")){
	 * redisOpsString.set("testStr", "redis..."); } }
	 */

	@Test
	public void testExists() {
		try {
			String key = "qumaIOOOPRRRo-login-redisKey_777";
			String is = redisOpsString.set(key, "1234");
			redisOpsString.expire(key, 999);
			System.out.println("String:" + is);
			// Assert.assertTrue("testStr这个key不存在", is);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testSet() {
		Assert.assertEquals("设置值失败", "OK", redisOpsString.set("testStr", "redis..."));
	}

	@Test
	public void testSetExpire() {
		redisOpsString.set("testStrExpire", "redis...5", 5);
		System.out.println("没有过期：" + redisOpsString.get("testStrExpire"));
		try {
			Thread.sleep(5001);
			System.out.println("过期后：" + redisOpsString.get("testStrExpire"));
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testAppend() {
		redisOpsString.append("testStr", "new append");
		String testStr = redisOpsString.get("testStr");
		System.out.println("追加后的字符串：" + testStr);
	}

	@Test
	public void testGetrange() {
		String rangeStr = redisOpsString.getRange("testStr", 0, 4);
		System.out.println("截取的字符串：" + rangeStr);
	}

	@Test
	public void testStrlen() {
		try {
			long strlen = redisOpsString.strlen("testStr");
			System.out.println("testStr长度：" + strlen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		String res = XxlConfClient.get("overall_public/redis.hostsAndPorts",""); //overall _public/
//		System.out.println("res:"+res);
//		String nodeKey = "overall_public/redis.hostsAndPorts";
//		if(nodeKey.indexOf("/")!=-1){
//    		String[] spitLists = nodeKey.split("/");
//    		String groupName = spitLists[0];
//			String nodeName = spitLists[1];
//			System.out.println("groupName:"+groupName+"  nodeName:"+nodeName);
//		}
	}
}
