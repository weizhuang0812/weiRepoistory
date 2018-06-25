//package com.foriseland.fjf.datasource;
//
//import java.lang.reflect.Method;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.aop.MethodBeforeAdvice;
//
//import com.foriseland.fjf.annotation.TradingDataSource;
//
///**
// * @Description: 动态切换数据源DataSourceBeforeAdvice通知类
// * @author wangHaiyang
// */
//// @Order(0)
//public class DataSourceBeforeAdvice implements MethodBeforeAdvice {
//	public void before(Method method, Object[] args, Object target) throws Throwable {
//		TradingDataSource tds = (TradingDataSource) target.getClass().getAnnotation(TradingDataSource.class);
//		System.out.println("###tds:"+tds);
//		if (tds == null) {
//			tds = (TradingDataSource) method.getAnnotation(TradingDataSource.class);
//		}
//		System.out.println("###tds -- method:"+tds);
//		if (tds != null) {
//			TradingDataSourceHolder.markMaster(); // 标记为写库
//		}
//	}
//
//}
