package com.foriseland.fjf.datasource;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description: 动态切换数据源TradingRoutingDataSource数据源切换类
 * @author wangHaiyang
 */
public class TradingRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return TradingDataSourceHolder.getDataSourceKey();
	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		afterPropertiesSet();
	}
}