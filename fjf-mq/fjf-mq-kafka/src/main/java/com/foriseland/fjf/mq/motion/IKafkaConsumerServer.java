package com.foriseland.fjf.mq.motion;

import com.foriseland.fjf.mq.exceptions.KafkaBaseException;

/**
 * @ClassName: com.foriseland.fjf.mq.motion
 * @Description: 是一个行为抽出来的行为接口.实现它会被消费者触发并执行
 * @author: wangHaiyang
 * @date 16/7/27
 */
public interface IKafkaConsumerServer {
	/**
	 * @title: execute 
	 * @param topic 队列
	 * @param partition 分区变量
	 * @param offset 偏移量
	 * @author: wangHaiyang 
	 * @createTime 2018年3月5日 下午4:35:34 
	 */
	
	public void run();// 函数启动方法
	
	public boolean execute(String topic,long offset) throws KafkaBaseException;
	public boolean execute(String topic,long offset, int partition) throws KafkaBaseException;
	public boolean execute(String topic,String groupId) throws KafkaBaseException;
	public boolean execute(String topic) throws KafkaBaseException;
	
}
