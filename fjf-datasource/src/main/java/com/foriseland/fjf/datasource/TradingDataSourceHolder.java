package com.foriseland.fjf.datasource;

/**
 * @Description: 动态切换数据源TradingDataSourceHolder数据源切换类
 * @author wangHaiyang
 */
public class TradingDataSourceHolder {

	// private static final ThreadLocal<String> threadDataSource = new
	// ThreadLocal<String>();
	private static final ThreadLocal<String> threadDataSource = new ThreadLocal<String>();

	// public static String getThreadDataSource() {
	//// String dataSourceName = (String) threadDataSource.get();
	// String dataSourceName = null;
	// Stack<String> stackSource = (Stack<String>) threadDataSource.get();
	// if(stackSource!=null){
	// dataSourceName = stackSource.peek();
	// }
	//
	// if (dataSourceName != null) {
	// // logger.info();
	// System.out.println("DataSource Name:[" + dataSourceName + "]");
	// }
	// return dataSourceName;
	// }

	/*
	 * public static void setThreadDataSource(String dataSourceName) {
	 * //threadDataSource.set(dataSourceName); Stack<String> stackSource =
	 * (Stack<String>) threadDataSource.get(); if(stackSource ==null){
	 * stackSource = new Stack<String>(); } stackSource.push(dataSourceName);
	 * threadDataSource.set(stackSource); }
	 */

	public static void setThreadDataSource(String key) {
		threadDataSource.set(key);
	}

	/**
	 * 标记写库
	 */
	public static void markMaster() {
		setThreadDataSource(DataSource.MASTER);
	}

	/**
	 * 标记读库
	 */
	public static void markSlave() {
		setThreadDataSource(DataSource.SLAVE);
	}
	
	public static boolean isMaster(){
		return Boolean.TRUE;
	}

	/**
	 * 获取数据源key
	 * 
	 * @return
	 */
	public static String getDataSourceKey() {
		return threadDataSource.get();
	}

	public static void clearThreadDataSource() {
		String stackSource = threadDataSource.get();
		threadDataSource.remove();
		System.out.println("TradingDataSourceHolder Method ClearThreadDataSource ##:[clearThreadDataSource] "+stackSource);
	}
}
