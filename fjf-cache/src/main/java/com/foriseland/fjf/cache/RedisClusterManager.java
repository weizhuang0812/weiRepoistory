package com.foriseland.fjf.cache;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

public class RedisClusterManager extends JedisCluster {
	private JedisClusterConnectionHandler connectionHandler;
	private int timeout;
	private int maxRedirections = 300;
	private final int expire = 3600*24; // 默认过期时间1天
	  
	public RedisClusterManager(Set<HostAndPort> jedisClusterNode, int timeout,GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, poolConfig);
		this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig,timeout);
		this.timeout = timeout;
	}

//	public Long zunionstore(final String dstkey, final String... sets) {
//	    return new JedisClusterCommand<Long>(connectionHandler, timeout, maxRedirections) {
//	      @Override
//	      public Long execute(Jedis connection) {
//	    	  long res = 0;
//	    	  try {
//	    		  res = connection.zunionstore(dstkey, sets);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	        return res;
//	      }
//	    }.run(dstkey);
//	}

	public String setObject(final byte[] key, final byte[] value) {
		return new JedisClusterCommand<String>(connectionHandler,maxRedirections) {
			@Override
			public String execute(Jedis connection) {
				String res = null;
				try {
					res = connection.setex(key, expire, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		}.run(new String(key));
	}
	
	public byte[] getObject(final byte[] key) {
		return new JedisClusterCommand<byte[]>(connectionHandler,maxRedirections) {
			@Override
			public byte[] execute(Jedis connection) {
				byte[] res = null;
				try {
					res = connection.get(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		}.run(new String(key));
	}
	
	public Long delObject(final byte[] key) {
		return new JedisClusterCommand<Long>(connectionHandler,maxRedirections) {
			@Override
			public Long execute(Jedis connection) {
				Long res = 0L;
				try {
					res = connection.del(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		}.run(new String(key));
	}
	
	
	public Long expire(final byte[] key,final int seconds){
		return new JedisClusterCommand<Long>(connectionHandler,maxRedirections) {
			@Override
			public Long execute(Jedis connection) {
				Long res = 0L;
				try {
					res = connection.expire(key, seconds);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		}.run(new String(key));
	}

    @SuppressWarnings("rawtypes")
	public void subscriber(String channel, JedisPubSub pubSub) {
         new JedisClusterCommand(connectionHandler,maxRedirections) {
            @Override
            public Long execute(Jedis connection) {
                Long res = 0L;
                try {
                    connection.subscribe(pubSub, channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }
        }.run(new String(channel));
    }



    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<String> keys(final String channel) {
        return (Set<String>) new JedisClusterCommand(connectionHandler,maxRedirections) {
            @Override
            public Long execute(Jedis connection) {
                Long res = 0L;
                try {
                    connection.keys(channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }
        }.run(new String(channel));
    }


    public Long published(String channel,String value){
        return new JedisClusterCommand<Long>(connectionHandler,maxRedirections) {
            @Override
            public Long execute(Jedis connection) {
                Long res = 0L;
                try {
                    return connection.publish(channel, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }
        }.run(new String(channel));
    }
}
