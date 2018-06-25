package com.foriseland.fjf.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.foriseland.fjf.datasource.DataSource;

/**
 * 数据源注解
 * 
 * @author wangHaiyang
 * 
 */
//@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MarkMasterDataSource {
	String sourceType() default DataSource.MASTER;
}
 