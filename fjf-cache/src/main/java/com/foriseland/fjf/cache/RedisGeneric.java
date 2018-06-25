package com.foriseland.fjf.cache;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisCluster集群的控制类
 * @author wangHaiyang
 *
 */
public class RedisGeneric{

	private RedisClusterManager jedisCluster = null;
	private JedisPoolConfig jedisPoolConfig;
	private int timeout = 3600*24; // 初始化超时-时间
	public int getTimeout() {
		return timeout;
	}

	private String clusterNodes;
	@SuppressWarnings("unused")
	private int maxRedirects = 300;
	
	public JedisPoolConfig poolConfig(){
		jedisPoolConfig = new JedisPoolConfig();
//		jedisPoolConfig.setMaxActive(1000);
		jedisPoolConfig.setMaxIdle(300);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(false);
		return jedisPoolConfig;
	}
	
	//	@Before
    public void init(){
        Set<HostAndPort> jedisClusterNodes = bindHostAndPort();
        System.out.println("[redis Hosts servers:]"+jedisClusterNodes);
        jedisCluster = new RedisClusterManager(jedisClusterNodes, timeout, poolConfig());
    }
    
    public Set<HostAndPort> bindHostAndPort(){
		String[]arrays = clusterNodes.split(",");
		if(null == arrays){
			return null;
		}
		Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
		for(String item : arrays){
			String[]subArray = item.split(":");
			if(null == subArray){
				continue;
			}
			if(subArray.length<1){
				continue;
			}
			HostAndPort bean = new HostAndPort(subArray[0],Integer.parseInt(subArray[1]));
			hostAndPortSet.add(bean);
		}
		return hostAndPortSet;
	}
    
    public void close() throws IOException{
    	jedisCluster.close();
    }

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public RedisClusterManager getJedisCluster() {
		return jedisCluster;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}
}