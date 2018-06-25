package com.foriseland.fjf.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.cache.exception.CacheException;
import com.foriseland.fjf.cache.rule.RedisKeyRule;
import com.foriseland.fjf.log.LogMgr;

/**
 * DistributedLock 分布式锁基础服务 <b>必要依赖：</b>
 * <p>fjf-cache</p>
 * <p>fjf-common</p>
 * @author wanghy
 * @date 2018-05-12 15:39
 */
@Component
public class DistributedLock {

	@Autowired
	protected DistributedLockManager redissonManager;

	/**
	 * @describe 对外服务加锁(提供验证和可重入锁[Reentrant Lock]设计,同时还支持自动过期解锁)
	 * @param key: <p>Lock key</p>
	 * @param leaseTime:<p>加锁时间,默认支持过期解锁功能,60秒钟以后自动解锁,无需调用unlock方法手动解锁</p>
	 * @return Boolean {true:success | false:error or CacheException}
	 * @throws CacheException
	 * @realization <b>实现原理：</b>
	 * <p><b>[第一步]：</b>验证generate lock key的命名格式,格式为：[object-model-key],否则抛出CacheException -> distributed to unLock is error </p>
	 * <p><b>[第二步]：</b>generateKey 组合</p>
	 * <p><b>[第三步]：</b>getLock并调用redissonLock.lock增加持有锁,如果leaseTime为空,则走系统默认,最多等待10秒，上锁以后10秒自动解锁</p>
	 * <p><b>[第四步]：</b>redissonLock.tryLock 尝试加锁，最多等待10秒，上锁以后10秒自动解锁</p>
	 * <p><b>[第五步]：</b>验证redissonLock.tryLock 返回值Boolean,如果返回True代表<b>[success]</b>,否则返回False代表<b>[error 或CacheException]</b></p>
	 */
	public boolean lock(String key, Integer leaseTime) throws CacheException {
		String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, key); //
		if (RedisKeyRule.isKeyRule(key) == Boolean.FALSE) {
			throw new CacheException("distributed to Lock is error,this key not effective,key value-> [" + key+ "],redis key be similar to [object-model-key]");
		}
		if (leaseTime == null) {
			leaseTime = DistributedConstat.REDI_SIMPLE_AUTO_TIME; // [走系统默认,最多等待10秒，上锁以后10秒自动解锁]
		}
		RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
		try {
			redissonLock.lock(DistributedConstat.REDI_SIMPLE_AUTO_TIME, TimeUnit.SECONDS);// [支持过期解锁功能,30秒钟以后自动解锁,
			// 无需调用unlock方法手动解锁]
			LogMgr.sysInfo("threadName:", Thread.currentThread().getName(), "DistributedLock Method",
					DistributedConstat.LOG_PREFIX, "LockKey:", lockKey);
			boolean isLock = redissonLock.tryLock(DistributedConstat.REDI_WAIT_TIME, leaseTime, TimeUnit.SECONDS); // [// 尝试加锁，最多等待10秒，上锁以后10秒自动解锁]
			if (isLock == Boolean.TRUE) {
				LogMgr.sysInfo("ToLock begin....", DistributedConstat.LOG_PREFIX, lockKey);
				return Boolean.TRUE; // [return -> 成功]
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			this.unLock(lockKey); // [防止死锁,强制释放]
			LogMgr.error(e.getMessage(), e);
			throw new CacheException(e.getMessage(), e);
		}
	}

	/**
	 * @describe 异步执行锁(提供验证和可重入锁[Reentrant Lock]设计,同时还支持自动过期解锁)
	 * @param key: <p>Lock key</p>
	 * @param leaseTime:<p>加锁时间,默认支持过期解锁功能,60秒钟以后自动解锁,无需调用unlock方法手动解锁</p>
	 * @return Boolean {true:success | false:error or CacheException}
	 * @throws CacheException
	 * @realization <b>实现原理：</b>
	 * <p>执行步骤请参考<b>lock说明</b> </p>
	 * <p><b>[第五步]：</b返回Future<Boolean>对象,通过Future.get()获得异步锁返回值,执行自己业务逻辑
	 */
	public Future<Boolean> asyncReentrantLock(String key,Integer leaseTime) throws CacheException {
		String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, key); //
		if (RedisKeyRule.isKeyRule(key) == Boolean.FALSE) {
			throw new CacheException("distributed to Lock is error,this key not effective,key value-> [" + key+ "],redis key be similar to [object-model-key]");
		}
		if (leaseTime == null) {
			leaseTime = DistributedConstat.REDI_SIMPLE_AUTO_TIME; // [走系统默认,最多等待10秒，上锁以后10秒自动解锁]
		}
		RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
		try {
			redissonLock.lockAsync();
			redissonLock.lockAsync(10, TimeUnit.SECONDS);
			Future<Boolean> futureResult = redissonLock.tryLockAsync(DistributedConstat.REDI_WAIT_TIME, leaseTime, TimeUnit.SECONDS); // [// 尝试加锁，最多等待10秒，上锁以后10秒自动解锁]
//			if (res.get()) {
//				// do your business
//			}
			return futureResult;
		} catch (Exception e) {
			e.printStackTrace();
		}   finally {
			redissonLock.unlock();
		}
		return null;
	}
	
	/**
	 * MultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
	 * @param lockKeys <p>Lock key</p>
	 * @return
	 * @realization <b>实现原理：</b>
	 * <p>执行步骤请参考<b>lock说明</b> </p>
	 * <p><b>[第五步]：</b返回multiLock 提供多个锁同时操作
	 * @throws CacheException
	 */
	public boolean multiLock(String ... lockKeys) throws CacheException {
		if (RedisKeyRule.isKeyRule(lockKeys) == Boolean.FALSE) {
			throw new CacheException("distributed to Lock is error,this key not effective,key value-> [" + lockKeys+ "],redis key be similar to [object-model-key]");
		}
		List<RLock> rLockLists = new ArrayList<RLock>();
		for(String item : lockKeys) {
			String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, item); //
			RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
			rLockLists.add(redissonLock);
		}
		RLock[] rLockArrays = new RLock[rLockLists.size()];
		rLockLists.toArray(rLockArrays);
		RedissonMultiLock redissonLock = new RedissonMultiLock(rLockArrays);
		try {
			redissonLock.lock(); //同时加锁：lock1,lock...n, 所有的锁都上锁成功才算成功。
			boolean isLock = redissonLock.tryLock(DistributedConstat.REDI_SIMPLE_AUTO_TIME, 10, TimeUnit.SECONDS); // [// 尝试加锁，最多等待10秒，上锁以后10秒自动解锁]
			return isLock;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			redissonLock.unlock();
		}
		return Boolean.FALSE;
	}

	/**
	 * @describe 解锁 [对应Lock接口]
	 * @param key:generate lock key
	 * @throws CacheException
	 * @realization <b>实现原理：</b>
	 * <p><b>[第一步]：</b>验证generate lock key 的命名格式,格式为：[object-model-key],否则抛出CacheException -> distributedto unLock is error</p>
	 * <p><b>[第二步]：</b>generateKey 组合</p>
	 * <p><b>[第三步]：</b>getLock并调用unlock释放锁</p>
	 */
	public void unLock(String key) throws CacheException {
		if (RedisKeyRule.isKeyRule(key) == Boolean.FALSE) { //
			throw new CacheException("distributed to unLock is error,this key not effective,key value-> [" + key+ "],redis key be similar to [object-model-key]");
		}
		String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, key);
		RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
		redissonLock.unlock();
		LogMgr.sysInfo("methodName:unLock", "->threadName", Thread.currentThread().getName(),DistributedConstat.LOG_PREFIX, "lockKey:", lockKey);

	}

	/**
	 * @describe 从新释放锁 (待废弃,需要优化为异步锁)
	 * @param key:generate lock key
	 * @throws CacheException
	 */
	@Deprecated
	public void releaseLock(String key) throws CacheException {
		if (RedisKeyRule.isKeyRule(key) == Boolean.FALSE) { //
			throw new CacheException("distributed to releaseLock is error,this key not effective,key value-> [" + key+ "],redis key be similar to [object-model-key]");
		}
		String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, key);
		RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
		redissonLock.forceUnlock();
		redissonLock.clearExpireAsync();
	}

	/**
	 * @describe 迫切的返回锁状态
	 * @param key:generate lock key,否则抛出CacheException -> distributed to unLock is error
	 * @return boolean:[true:代表已被锁 | false:未被锁 ]
	 * @throws CacheException
	 * @realization <b>实现原理：</b>
	 * <p><b>[第一步]：</b>验证generate lock key 的命名格式,格式为：[object-model-key]</p>
	 * <p><b>[第二步]：</b>generateKey 组合</p>
	 * <p><b>[第三步]：</b>[if]:当前锁状态未释放,则直接返回@TODO后期会根据需求增加对锁的订阅</p>
	 * <p><b>[第四步]：</b> 如果订阅锁,则根据线程订阅时间,优先选择通知顺序... @TODO 后期增加</p>
	 */
	public boolean pressingReturnLock(String key) throws CacheException {
		if (RedisKeyRule.isKeyRule(key) == Boolean.FALSE) { //
			throw new CacheException("distributed to pressingReturnLock is error,key value-> ["+ key + "],redis key be similar to [object-model-key]");
		}
		String lockKey = generateKey(DistributedConstat.LOCK_SUFFIX, key);
		RLock redissonLock = redissonManager.getRedisson().getLock(lockKey);
		if (redissonLock.isLocked() == Boolean.TRUE) {
			return Boolean.TRUE;
		}
		LogMgr.sysInfo("methodName:pressingReturnLock", "->threadName", Thread.currentThread().getName(),DistributedConstat.LOG_PREFIX, "lockKey:", lockKey);
		return Boolean.FALSE;
	}

	/**
	 * @describe 将多个标识组合返回
	 * @param args
	 * @return
	 */
	private String generateKey(Object... args) {
		StringBuilder builder = new StringBuilder();
		for (Object arg : args) {
			builder.append(arg);
		}
		return builder.toString();
	}
}
