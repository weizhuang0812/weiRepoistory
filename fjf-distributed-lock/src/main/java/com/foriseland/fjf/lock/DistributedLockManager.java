package com.foriseland.fjf.lock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.cache.RedisGeneric;
import com.foriseland.fjf.cache.exception.CacheException;

import redis.clients.jedis.HostAndPort;
@Component
public class DistributedLockManager {
	private  Config config = new Config();
	private  RedissonClient redisson = null;

	@Autowired
	private RedisGeneric redisGeneric;


	public String[] bindHostAndPort() throws Exception {
		Set<HostAndPort> hostAndPortSet = redisGeneric.bindHostAndPort();
		if (hostAndPortSet == null) {
			throw new Exception("RedissonManager HostAndPort is Null");
		}
		List<String> lists = new ArrayList<String>();
		Iterator<HostAndPort> it = hostAndPortSet.iterator();
		while (it.hasNext()) {
			HostAndPort itemValue = it.next();
			String appendHosts ="redis://"+ itemValue.getHost() + ":" + itemValue.getPort();
			lists.add(appendHosts);
		}
		String[] strings = new String[lists.size()];
		lists.toArray(strings);
		return strings;
	}

	@PostConstruct
	public void init() throws CacheException {
		try {
			config.useClusterServers() // 这是用的集群server
					.setScanInterval(2000) // 设置集群状态扫描时间
					.setMasterConnectionPoolSize(10000) // 设置连接数 bindHostAndPort()
					.setSlaveConnectionPoolSize(10000).addNodeAddress(bindHostAndPort());
			redisson = Redisson.create(config);
			// 清空自增的ID数字
			RAtomicLong atomicLong = redisson.getAtomicLong(DistributedConstat.RAtomicName);
			atomicLong.set(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException(e.getMessage());
		}
	}

	public RedissonClient getRedisson() {
		return redisson;
	}

	/** 获取redis中的原子ID */
	public Long nextID() {
		RAtomicLong atomicLong = getRedisson().getAtomicLong(DistributedConstat.RAtomicName);
		atomicLong.incrementAndGet();
		return atomicLong.get();
	}
	
	/** 
     * 关闭Redisson客户端连接 
     * @param redisson2 
     */  
    private void closeRedisson(RedissonClient redisson2){  
        redisson2.shutdown();  
        System.out.println("成功关闭Redis Client连接");  
    }  
    
    @After
	public void after() {
		this.closeRedisson(redisson);
	}
}