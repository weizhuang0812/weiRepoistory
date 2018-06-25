package com.foriseland.fjf.cache.redis.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foriseland.fjf.cache.support.RedisGeo;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-redis-test.xml" })
public class RedisGeoTest {

	@Autowired
	public RedisGeo redisGeo;

	/*
	 * @Before public void init(){ if(!redisOpsString.exists("testStr")){
	 * redisOpsString.set("testStr", "redis..."); } }
	 */

	@Test
	public void testGeoadd() {
		try {
			String key = "crowd1";
			GeoCoordinate geoCoordinate=new GeoCoordinate(116.41667, 39.91667);
			String memberName="北京";
			Long geoadd = redisGeo.geoadd(key, geoCoordinate, memberName);
			System.out.println("String:" + geoadd);
			// Assert.assertTrue("testStr这个key不存在", is);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGeoadds() {
		try {
			String key = "crowd2";
			 Map<String, GeoCoordinate> memberCoordinateMap=new HashMap<String, GeoCoordinate>();
			 GeoCoordinate geoCoordinate=new GeoCoordinate(121.43333,34.50000);
			 GeoCoordinate geoCoordinate1=new GeoCoordinate(117.20000,39.13333);
			 GeoCoordinate geoCoordinate2=new GeoCoordinate(116.41667, 39.91667);
			 memberCoordinateMap.put("上海", geoCoordinate);
			 memberCoordinateMap.put("天津", geoCoordinate1);
			 memberCoordinateMap.put("北京", geoCoordinate2);
			 Long geoadd = redisGeo.geoadd(key, memberCoordinateMap);
			System.out.println("long" + geoadd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void geoRadiusTest() {
		//String key, GeoCoordinate coordinate, double radius
		String key="crowd2";
		GeoCoordinate coordinate=new GeoCoordinate(116.41667, 39.91667);
		double radius=150;
		List<GeoRadiusResponse> geoRadius = redisGeo.geoRadius(key, coordinate, radius);
		for (GeoRadiusResponse geoRadiusResponse : geoRadius) {
			System.out.println("附近的地理位置:" + geoRadiusResponse.getCoordinate());
		}
	}

	@Test
	public void testGeoradiusByMember() {
		//String key, String member, double radius
		String key="crowd2";
		String member="天津";
		double radius=150;
		List<GeoRadiusResponse> georadiusByMember = redisGeo.georadiusByMember(key, member, radius);
		for (GeoRadiusResponse geoRadiusResponse : georadiusByMember) {
			System.out.println("附近的地理位置:" + geoRadiusResponse.getCoordinate());
		}
	}

	@Test
	public void testGeoDist() {
		//String key, String member1, String member2, GeoUnit unit
		String key="crowd2";
		String member1="天津";
		String member2="北京";
		GeoUnit unit=GeoUnit.KM;
		Double geoDist = redisGeo.geoDist(key, member1, member2, unit);
		System.out.println("两地之间的距离:"+geoDist);
	}
	
	@Test
	public void testGeohash() {
		//String key, String... members
		String key="crowd2";
		String member1="天津";
		String member2="北京";
		List<String> geohash = redisGeo.geohash(key, member1,member2);
		System.out.println("地理位置的hash值:"+geohash);
	}
	
	@Test
	public void testGeopos() {
		//String key, String... members
		String key="crowd2";
		String member1="天津";
		String member2="北京";
		List<GeoCoordinate> geopos = redisGeo.geopos(key, member1,member2);
		System.out.println("地理位置的hash值:"+geopos);
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
