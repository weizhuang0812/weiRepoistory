package com.foriseland.fjf.mq.motion;

import java.util.Properties;

/**
 * Kafka参数配置
 * @className: IKafkaConsumerConfig 
 * @describe: @TODO(这里用一句话描述这个类的作用) 
 * @author: PC  
 * @createTime 2018年3月24日 上午10:39:27
 */
public interface IKafkaConsumerConfig {

	public void setProperties(Properties properties);
	public Properties getProperties();
}
