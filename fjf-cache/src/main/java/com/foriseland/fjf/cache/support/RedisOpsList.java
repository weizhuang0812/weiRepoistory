package com.foriseland.fjf.cache.support;

import java.util.List;

import org.springframework.stereotype.Service;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * @className: RedisOpsList 
 * @describe: Redis结果之-List集合类服务包
 * @author: wangHaiyang  
 * @createTime 2017年12月4日 下午2:14:13
 */
@Service
public class RedisOpsList extends BaseRedisCache {
	
	/**
	 * 判断key是否存在
	 * @param String  key
	 * @return boolean
	 */
	public boolean exists(String key) {
		boolean exis = redisGeneric.getJedisCluster().exists(key);
		return exis;
	}
	
	/**
	 * 将一个或多个值 value插入到列表 key的左侧
	 * @param key
	 * @param values
	 * @return
	 */
	public Long lpush(String key, String... values) {
		return redisGeneric.getJedisCluster().lpush(key, values);
	}
	
	/**
	 * 将一个或多个值 value插入到列表 key的右侧
	 * @param key
	 * @param values
	 * @return
	 */
	public Long rpush(String key, String... values) {
		return redisGeneric.getJedisCluster().rpush(key, values);
	}
	
	/**
	 * 获取指定范围的元素列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end){
		return redisGeneric.getJedisCluster().lrange(key, start, end);
	}
	
	/**
	 * 获取列表的长度
	 * @param key
	 * @return
	 */
	public Long llen(String key){
		return redisGeneric.getJedisCluster().llen(key);
	}
	
	/**
	 * 获取指定的元素
	 * @param key
	 * @param index
	 * @return
	 */
	public String lindex(String key, long index){
		return redisGeneric.getJedisCluster().lindex(key, index);
	}
	
	/**
	 * 更新列表中指定位置的值为新值
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public String lset(String key, long index, String newValue){
		return redisGeneric.getJedisCluster().lset(key, index, newValue);
	}
	
	/**
	 * 弹出列表左侧的一个元素
	 * @param key
	 * @return
	 */
	public String lpop(String key){
		return redisGeneric.getJedisCluster().lpop(key);
	}
	
	/**
	 * 弹出列表右侧的一个元素
	 * @param key
	 * @return
	 */
	public String rpop(String key){
		return redisGeneric.getJedisCluster().rpop(key);
	}
	
	/**
	 * 列表只保留指定区间内的元素
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String ltrim(String key, long start, long end){
		return redisGeneric.getJedisCluster().ltrim(key, start, end);
	}
	
	/**
	 * 将值 value插入到列表key当中，位于值 pivot之前或之后。
	 * @param key
	 * @param where(BEFORE|AFTER)
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long linsert(String key, LIST_POSITION where, String pivot, String value){
		return redisGeneric.getJedisCluster().linsert(key, where, pivot, value);
	}
	
	/**
	 * 删除指定个元素
	 * @param key
	 * @param count（大于0从头向尾遍历并删除，小于0从尾向头遍历并删除， 等于0删除所有等于value的元素）
	 * @param value
	 * @return 删除的个数
	 */
	public Long lrem(String key, long count, String value){
		return redisGeneric.getJedisCluster().lrem(key, count, value);
	}
}
