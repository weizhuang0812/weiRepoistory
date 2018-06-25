package com.foriseland.fjf.mq.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.foriseland.fjf.mq.consumer.BaseConsumerRunnable;
import com.foriseland.fjf.mq.motion.IKafkaConsumerServer;
import com.foriseland.fjf.util.SpringContextUtil;

public class ConsumerManagerPool {
	
	/**
     * 消费者线程管理者.
     */
    private ExecutorService consumerManager;
    
    /**
     * 消费者列表引用
     */
    private List<Object> consumers;
    
    public void start() {
    	this.initThreadPool(); // step1: [初始化线程池]
    	this.initConsumers(); //  step2: [初始化反射需要的Consumer对象集合]
    	this.initStart(); // step3:[初始化多线程执行]
    }
    
    
    private List<Object> initConsumers(){
    	consumers = this.searchBeanNamesForType();
    	return consumers;
    }
    
    /**
     * @title: initThreadPool 初始化线程池
     * @createTime 2018年3月24日 下午12:37:02 
     * @throws
     */
    public void initThreadPool() {
    	 consumerManager = Executors.newCachedThreadPool();
    }
    
    private void initStart() {
    	for(Object invodeBean : consumers) {
    		BaseConsumerRunnable baseInvokeBean = new BaseConsumerRunnable(invodeBean);
    		consumerManager.execute(baseInvokeBean); // 开启代理执行
    	}
    }
    
    
    /**
     * @title: beanNamesForType 获得接口所有实例
     * @createTime 2018年3月24日 下午12:35:19 
     * @throws
     */
	public List<Object> searchBeanNamesForType() {
		List<Object> consumers = new ArrayList<>();
		String[] result = SpringContextUtil.getApplicationContext().getBeanNamesForType(IKafkaConsumerServer.class);
		for(String item : result) {
			Object invodeBean = SpringContextUtil.getBean(item);
			consumers.add(invodeBean);
		}
		return consumers;
	}
	
}
