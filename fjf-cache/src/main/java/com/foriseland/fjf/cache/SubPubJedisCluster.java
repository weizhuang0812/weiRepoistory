package com.foriseland.fjf.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.Set;

/**
 * @ClassName: com.foriseland.fjf.cache
 * @Description:
 * @author: wangHaiyang
 * @date 2016/11/10
 */
public class SubPubJedisCluster extends RedisClusterManager {

    public SubPubJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxRedirections, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, poolConfig);
    }


}
