package com.foriseland.fjf.cache.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.foriseland.fjf.cache.exception.CacheException;
import com.foriseland.fjf.cache.rule.RedisKeyRule;

/**
 * @ClassName: com.foriseland.fjf.aop
 * @Description:AopApiParameterValidation
 * @author: wangHaiyang
 * @date 2017/12/01
 */
@Aspect
@Component
public class AopRedisRuleAspect {

	/**
	 * 在进入Service方法之前执行
	 * 
	 * @param JoinPoint
	 *            切面对象
	 * @throws CacheException
	 */
	/*
	 * @Before("execution(* com.foriseland.*..*support..*.*(..))") public void
	 * before(JoinPoint point) throws CacheException { Object key =
	 * point.getArgs()[0]; boolean isKeyRule = RedisKeyRule.isKeyRule(key);
	 * if(isKeyRule == Boolean.FALSE){ System.out.println("JoinPoint - value");
	 * throw new
	 * CacheException("redis Key is not effective,key be similar to [object-model-key]"
	 * ); } }
	 */

	@Before("execution(* com.foriseland.*..*support..*.*(..))")
	// @Before(value = "com.foriseland.fjf.cache.support.RedisOpsString(..)")
	public void before(JoinPoint joinPoint) throws CacheException {
		Object[] keys = joinPoint.getArgs();
		if (null != keys && keys.length > 0) {
			Object key = joinPoint.getArgs()[0];
			boolean isKeyRule = RedisKeyRule.isKeyRule(key);
			if (isKeyRule == Boolean.FALSE) {
				CacheException ex = new CacheException("redis Key is not effective,key be similar to [object-model-key]");
				ex.printStackTrace();
				throw ex;
			}
		}
	}
}