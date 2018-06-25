package com.foriseland.fjf.datasource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

import com.foriseland.fjf.annotation.MarkMasterDataSource;
import com.foriseland.fjf.exception.BaseExcepton;
import com.foriseland.fjf.sequence.util.ReflectHelper;

/**
 * @className: DataSourceAspect
 * @describe: AOP切面控制类
 *            <p>
 *            定义数据源的AOP切面，该类控制了使用Master还是Slave。
 *            </p>
 *            <p>
 *            如果事务管理中配置了事务策略，则采用配置的事务策略中的标记了ReadOnly的方法是用Slave，其它使用Master。
 *            </p>
 *            <p>
 *            如果没有配置事务管理的策略，则采用方法名匹配的原则，以query、find、get开头方法用Slave，其它用Master。
 *            </p>
 * @author wangHaiyang
 * @createTime 2017年12月25日 下午6:53:54
 */
public class DataSourceAspect {

	private List<String> slaveMethodPattern = new ArrayList<String>();

	private static final String[] DEFAULT_SLAVE_METHOD_START = new String[] { "query", "find", "get", "search","select" };
	private String[] saveSqlId = new String[] { "insert*", "save*" }; // mapper.xml中需要拦截的ID(正则匹配)

	private String[] slaveMethodStart;

	/**
	 * 读取事务管理中的策略
	 * 
	 * @param txAdvice
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setTxAdvice(TransactionInterceptor txAdvice) throws Exception {
		if (txAdvice == null) {
			return; // 没有配置事务管理策略
		}
		TransactionAttributeSource transactionAttributeSource = txAdvice.getTransactionAttributeSource(); // 从txAdvice获取到策略配置信息(tx:advice
																											// id="txAdvice")
		if (!(transactionAttributeSource instanceof NameMatchTransactionAttributeSource)) {
			return;
		}
		// 使用反射技术获取到NameMatchTransactionAttributeSource对象中的nameMap属性值
		NameMatchTransactionAttributeSource matchTransactionAttributeSource = (NameMatchTransactionAttributeSource) transactionAttributeSource;
		Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
		nameMapField.setAccessible(true); // 设置该字段可访问
		Map<String, TransactionAttribute> map = (Map<String, TransactionAttribute>) nameMapField
				.get(matchTransactionAttributeSource); // 获取nameMap的值

		// 遍历nameMap
		for (Map.Entry<String, TransactionAttribute> entry : map.entrySet()) {
			if (!entry.getValue().isReadOnly()) {// 判断之后定义了ReadOnly的策略才加入到slaveMethodPattern
				continue;
			}
			slaveMethodPattern.add(entry.getKey());
		}
	}

	/**
	 * 在进入Service方法之前执行
	 * 
	 * @param JoinPoint
	 *            切面对象
	 * @throws BaseExcepton
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	public void before(JoinPoint point) throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, BaseExcepton {
		MethodSignature joinPointObject = (MethodSignature) point.getSignature();
		Method method = joinPointObject.getMethod();
		MarkMasterDataSource tds = (MarkMasterDataSource) method.getAnnotation(MarkMasterDataSource.class);
		if (tds != null) {
			TradingDataSourceHolder.markMaster(); // 强制设置为markMaster
			return;
		}
		boolean isSlave = false;
		String methodName = point.getSignature().getName(); // 获取到当前执行的方法名
		if (slaveMethodPattern.isEmpty()) {
			isSlave = this.isSlave(methodName); // 当前Spring容器中没有配置事务策略，采用方法名匹配方式
		} else {
			for (String mappedName : slaveMethodPattern) { // 使用策略规则匹配
				if (isMatch(methodName, mappedName)) {
					isSlave = true;
					break;
				}
			}
		}
		if (isSlave == Boolean.TRUE) {
			TradingDataSourceHolder.markSlave(); // 标记为读库 |(从库)
		} else {
			TradingDataSourceHolder.markMaster(); // 标记为写库 |(主库)
			Object[] args = point.getArgs(); // 获得参数
			if (args != null && args.length > 0) {
				this.setSeqValue(methodName, args); // 反射生成seq
			}
		}
	}

	/**
	 * 判断是否为读库 -> [方法名以query、find、get开头的方法名走从库]
	 * 
	 * @param methodName
	 * @return
	 */
	private Boolean isSlave(String methodName) {
		return StringUtils.startsWithAny(methodName, getSlaveMethodStart());
	}

	public void clearThreadDataSource(JoinPoint point) {
		TradingDataSourceHolder.clearThreadDataSource();
	}

	/**
	 * 通配符匹配
	 * 
	 * @param methodName
	 *            方法名
	 * @param mappedName
	 *            映射比较方法名
	 */
	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}

	/**
	 * 用户指定slave的方法名前缀
	 * 
	 * @param slaveMethodStart
	 */
	public void setSlaveMethodStart(String[] slaveMethodStart) {
		this.slaveMethodStart = slaveMethodStart;
	}

	public String[] getSlaveMethodStart() {
		if (this.slaveMethodStart == null) { // 没有指定，使用默认
			return DEFAULT_SLAVE_METHOD_START;
		}
		return slaveMethodStart;
	}

	public void setSeqValue(String methodName, Object[] args) throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException, BaseExcepton {
		for (String mappedName : saveSqlId) {
			if (isMatch(methodName, mappedName)) {
				Object obj = args[0];
				ReflectHelper.setSeqValue(obj);
			}
		}
	}
}