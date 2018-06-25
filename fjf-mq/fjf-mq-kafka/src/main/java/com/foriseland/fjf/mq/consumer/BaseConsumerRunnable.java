package com.foriseland.fjf.mq.consumer;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.util.ReflectionUtils;

public class BaseConsumerRunnable implements Runnable{
	
    /**
     * 线程轮询开关
     */
    private AtomicBoolean running = new AtomicBoolean(Boolean.TRUE); 
    
    private Object invokeBean;
    
    public BaseConsumerRunnable(Object invokeBean) {
    	this.invokeBean = invokeBean;
    }
    
    /**
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws ClassNotFoundException 
	 * @title: invokeMethod  通过代理执行run 方法
	 * @createTime 2018年3月24日 下午12:03:12 
	 * @throws
	 */
	private void invokeMethod(Object invokeBean) {
		 try {
			 Class<?> clazz = Class.forName(invokeBean.getClass().getName());  
			 Method method = ReflectionUtils.findMethod(clazz, "run");
			 ReflectionUtils.invokeMethod(method,invokeBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("多线程执行...");
		this.invokeMethod(invokeBean);
		
	}
	
    public void close() {
        running.set(Boolean.FALSE);
//        this.kafkaConsumer.wakeup();
    }

}
