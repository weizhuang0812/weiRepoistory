package com.foriseland.fjf.cache.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foriseland.fjf.cache.RedisGeneric;

/**
 * 
 * @className: RedisOpsMap 
 * @describe:Redis结果之-Map类操作
 * @author: wangHaiyang  
 * @createTime 2018年1月16日 上午11:53:52
 */
@Service
public class RedisOpsMap extends BaseRedisCache{

	@Autowired
	private RedisGeneric redisGeneric;
	
	/**
	 * 设置值
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value){
		redisGeneric.getJedisCluster().hset(key, field, value);
	}
	
	/**
	 * 获取值
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field){
		return redisGeneric.getJedisCluster().hget(key, field);
	}
	
	/**
     * 设置 key.field 的增量
     */
    public void hincrby(String key, String field, int value) {
        redisGeneric.getJedisCluster().hincrBy(key, field, value);
    }
	
	/**
	 * 判断是否存在 key 中，给定域 field 是否存在
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key, String field){
		return redisGeneric.getJedisCluster().hexists(key, field);
	}
	
	/**
	 * 获取所有值
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key){
		return redisGeneric.getJedisCluster().hgetAll(key);
	}
	
	/**
	 * 同时将多个 field-value
	 * @param key
	 * @param fieldValues
	 */
	public void hmset(String key, Map<String, String> fieldValues){
		redisGeneric.getJedisCluster().hmset(key, fieldValues);
	}
	
	/**
	 * 获取指定的值
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(String key, String... fields){
		if(null != fields && fields.length>0){
			return redisGeneric.getJedisCluster().hmget(key, fields);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 获取长度
	 * @param key
	 * @return
	 */
	public long hlen(String key){
		return redisGeneric.getJedisCluster().hlen(key);
	}
	
	/**
	 * 获取key所有的fields
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key){
		return redisGeneric.getJedisCluster().hkeys(key);
	}
	
	/**
	 * 获取key所有的values
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key){
		return redisGeneric.getJedisCluster().hvals(key);
	}
	
	public long hdel(String key, String... field){
		if(null != field && field.length>0){
			return redisGeneric.getJedisCluster().hdel(key, field);
		}
		return 0;
	}
}
