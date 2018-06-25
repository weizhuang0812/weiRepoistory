package com.foriseland.fjf.cache.support;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foriseland.fjf.cache.RedisGeneric;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/**
 * 
 * @ClassName: RedisGeo (redis地理位置)
 * @Description: 
 * @author 朱自强 
 * @date 2018年3月6日 上午10:15:28
 */
@Service
public class RedisGeo extends BaseRedisCache{
	
	@Autowired
	private RedisGeneric redisGeneric;
	
	 /** 
     * 增加地理位置的坐标 
     * @param key 
     * @param coordinate 
     * @param memberName 
     * @return 
     */  
    public Long geoadd(String key, GeoCoordinate coordinate, String memberName) { 
        return redisGeneric.getJedisCluster().geoadd(key, coordinate.getLongitude(), coordinate.getLatitude(), memberName);  
    }
    
    /** 
     * 批量添加地理位置 
     * @param key 
     * @param memberCoordinateMap 
     * @return 
     */  
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap){  
        return redisGeneric.getJedisCluster().geoadd(key, memberCoordinateMap);  
    }
    
    /** 
     * 根据给定地理位置坐标获取指定范围内的地理位置集合（返回匹配位置的经纬度 + 匹配位置与给定地理位置的距离 + 从近到远排序，） 
     * @param key 
     * @param coordinate 
     * @param radius 
     * @return  List<GeoRadiusResponse> 
     */  
    public List<GeoRadiusResponse> geoRadius(String key, GeoCoordinate coordinate, double radius) {  
        return redisGeneric.getJedisCluster().georadius(key, coordinate.getLongitude(), coordinate.getLatitude(), radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().withDist().withCoord().sortAscending());  
    }  
    
    /** 
     * 根据给定地理位置获取指定范围内的地理位置集合（返回匹配位置的经纬度 + 匹配位置与给定地理位置的距离 + 从近到远排序，） 
     * @param key 
     * @param member 
     * @param radius 
     * @return  List<GeoRadiusResponse> 
     */  
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius){  
        return redisGeneric.getJedisCluster().georadiusByMember(key, member, radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().withDist().withCoord().sortAscending());  
    }
    
    /** 
     * 查询两位置距离 
     * @param key 
     * @param member1 
     * @param member2 
     * @param unit 
     * @return 
     */  
    public Double geoDist(String key, String member1, String member2, GeoUnit unit){  
        return redisGeneric.getJedisCluster().geodist(key, member1, member2, unit);  
    }  
    
    /** 
     * 可以获取某个地理位置的geohash值 
     * @param key 
     * @param members 
     * @return 
     */  
    public List<String> geohash(String key, String... members){  
        return redisGeneric.getJedisCluster().geohash(key, members);  
    }  
    
    /** 
     * 获取地理位置的坐标 
     * @param key 
     * @param members 
     * @return 
     */  
    public List<GeoCoordinate> geopos(String key, String... members){  
        return redisGeneric.getJedisCluster().geopos(key, members);  
    }  

}
