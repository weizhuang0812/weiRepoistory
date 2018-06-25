package com.foriseland.fjf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 标识MyBatis的DAO,方便{@link org.mybatis.spring.mapper.MapperScannerConfigurer}的扫描
 * @author wangHaiyang
 * @date 2017/12/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transactional(isolation = Isolation.REPEATABLE_READ) 
public @interface MyBatisRepository {
}
 