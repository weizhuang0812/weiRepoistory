package com.foriseland.fjf.datasource;

import java.util.HashMap;
import com.alibaba.druid.util.JdbcUtils;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @Description: 动态切换数据源BeanPostProcessor装载类
 * @author wangHaiyang
 *
 */
public class BeanPostPrcessorImpl implements BeanPostProcessor {
	
	@Autowired 
	private TradingRoutingDataSource tradingRoutingDataSource;
	private static Map<Object,Object> targetDataSources =new HashMap<Object, Object>();
    public static Map<Object, Object> getTargetDataSources() {
		return targetDataSources;
	}
 
	public static void setTargetDataSources(Map<Object, Object> targetDataSources) {
		BeanPostPrcessorImpl.targetDataSources = targetDataSources;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof DruidDataSource) {
			System.out.println("targetDataSources -> beanName:"+beanName);
			targetDataSources.put(beanName, bean);
			tradingRoutingDataSource.setTargetDataSources(targetDataSources);
		}
		return bean;
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
