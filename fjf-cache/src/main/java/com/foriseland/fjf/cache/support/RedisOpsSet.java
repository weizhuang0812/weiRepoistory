package com.foriseland.fjf.cache.support;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foriseland.fjf.cache.RedisGeneric;

import redis.clients.jedis.Tuple;

/**
 * @className: RedisOpsSet 
 * @describe: Redis结果之-Set类操作API
 * @author: wangHaiyang  
 * @createTime 2018年1月16日 上午11:52:51
 */
@Service
public class RedisOpsSet extends BaseRedisCache{

	@Autowired
	private RedisGeneric redisGeneric;
	
	/**
	 * 判断key是否存在
	 * 
	 * @param String  key
	 * @return boolean
	 */
	public boolean exists(String key) {
		boolean exis = redisGeneric.getJedisCluster().exists(key);
		return exis;
	}
	
	/**
	 * @title: sadd 
	 * @describe: sets中添加元素
	 * @param key Set的Key
	 * @param member Values value
	 * @throws
	 */
	public Long sadd(String key, String... values) {
		return redisGeneric.getJedisCluster().sadd(key, values);
	}
	
	/**
	 * @title: smembers 
	 * @describe: 查看sets集合中的所有元素
	 * @param key Set的Key
	 * @throws
	 */
	public Set<String> smembers(String key) {
		return redisGeneric.getJedisCluster().smembers(key);
	}
	
	/**
	 * 将一个或多个 member元素及其score值加入到有序集 key当中
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Long zadd(String key, double score, String member){
		return redisGeneric.getJedisCluster().zadd(key, score, member);
	}
	
	/**
	 * 将一个或多个 member元素及其score值加入到有序集 key当中
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	public Long zadd(String key, Map<String, Double> scoreMembers){
		return redisGeneric.getJedisCluster().zadd(key, scoreMembers);
	}
	
	/**
	 * 指定范围的列表集合
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zrange(String key, long start, long end){
		return redisGeneric.getJedisCluster().zrange(key, start, end);
	}
	
	/**
	 * 指定范围的列表集合（倒序）
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrevrange(String key, long start, long end){
		return redisGeneric.getJedisCluster().zrevrange(key, start, end);
	}
	
	/**
	 * 指定范围的列表集合成员和得分
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrangeWithScores(String key, long start, long end){
		return redisGeneric.getJedisCluster().zrangeWithScores(key, start, end);
	}
	
	/**
	 * 返回有序集的基数
	 * @param key
	 * @return
	 */
	public Long zcard(String key){
		return redisGeneric.getJedisCluster().zcard(key);
	}
	
	/**
	 * 获取指定范围中集合元素个数
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zcount(String key, String min, String max){
		return redisGeneric.getJedisCluster().zcount(key, min, max);
	}
	
	/**
	 * 为有序集key的成员 member的 score值加上增量increment 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Double zincrby(String key, double score, String member){
		return redisGeneric.getJedisCluster().zincrby(key, score, member);
	}
	
	/**
	 * 获取成员 member的 score值
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zscore(String key, String member){
		return redisGeneric.getJedisCluster().zscore(key, member);
	}
	
	/**
	 * 返回有序集 key 中成员 member 的排名
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrank(String key, String member){
		return redisGeneric.getJedisCluster().zrank(key, member);
	}
	
	/**
	 * 返回有序集 key中成员 member的倒序排名
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrevrank(String key, String member){
		return redisGeneric.getJedisCluster().zrevrank(key, member);
	}
	
	/**
	 * 移除有序集 key中的一个或多个成员，不存在的成员将被忽略
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrem(String key, String... member){
		return redisGeneric.getJedisCluster().zrem(key, member);
	}
	
	/**
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	/*public Long zunionstore(String dstkey, String... sets){
		return redisGeneric.getJedisCluster().zunionstore(dstkey, sets);
	}*/
	
	/**
	 * @title: sinter 
	 * @describe:返回给定所有集合的交集
	 * @author: wangHaiyang 
	 * @createTime 2018年3月13日 上午9:58:30 
	 * @throws
	 */
	public Set<String> sinter(String ... keys) {
		return redisGeneric.getJedisCluster().sinter(keys);
	}
	
	 /** 
	  * @title: sinter 
      * 返回所有给定集合的并集，相同的只会返回一个 
      * @param setKeys 
      * @return 
     */  
	public Set<String> sunion(String ... setKeys) {
		return redisGeneric.getJedisCluster().sunion(setKeys);
	}
	
	/** 
	  * @title: sdiff 
      * 返回给定所有集合的差集（获取第一个key中与其它key不相同的值，当只有一个key时，就返回这个key的所有值） 
      * @param setKeys 
      * @return 
    */ 
	public Set<String> sdiff(String ... setKeys) {
		return redisGeneric.getJedisCluster().sdiff(setKeys);
	}
	
	/**
	 *  @title: srem 移除Set集合指定元素 
	 * @param key 
	 * @param member [数组]
	 * @return
	 */
	public Long srem(String key,String... member) {
		return redisGeneric.getJedisCluster().srem(key, member);
	}
	
}