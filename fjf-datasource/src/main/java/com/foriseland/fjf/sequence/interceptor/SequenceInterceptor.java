package com.foriseland.fjf.sequence.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.foriseland.fjf.exception.BaseExcepton;
import com.foriseland.fjf.sequence.annotation.SequenceField;
import com.foriseland.fjf.sequence.handle.SequenceUtil;
import com.foriseland.fjf.sequence.util.ReflectHelper;

/**
 * @description: 序列拦截器 ，对sqlid 为save的sql语句进行拦截，设置注解
 * @author 王海洋
 * @date 2016-08-16 下午2:44:50
 * @version V1.0
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class SequenceInterceptor implements Interceptor {

	private String saveSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)

	@Override
	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			Object oo = invocation.getTarget();
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) oo;
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,"mappedStatement");
			if (mappedStatement.getId().matches(saveSqlId)) {
				// BoundSql boundSql = delegate.getBoundSql();
				BoundSql boundSql = statementHandler.getBoundSql();// 原始sql对象
				Object parameterObject = boundSql.getParameterObject();
				System.out.println("###intercept###[Insert_SQL]:" + boundSql.getSql());
				System.out.println("###intercept###[Insert_parameterObject]:" + parameterObject);
				// 为批量插入时
				if (parameterObject instanceof Map) {
					Collection col = ((Map) parameterObject).values();
					for (Object obj : col) {
						if (obj instanceof Collection) {
							// 循环设置主键
							for (Object obj1 : (Collection) obj) {
								setSeqValue(obj1);
							}
						} else {
							setSeqValue(obj);
						}
					}
				} else {
					setSeqValue(parameterObject);
				}
			}
		}
		return invocation.proceed();
	}

	/**
	 * 设置主键
	 * 
	 * @param obj
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws BaseExcepton
	 */
	private void setSeqValue(Object obj) throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, BaseExcepton {
		Field[] fileds = obj.getClass().getDeclaredFields();
		SequenceUtil sequenceUtil = SequenceUtil.create();
		for (Field field : fileds) {
			SequenceField sf = field.getAnnotation(SequenceField.class);
			if (null != sf) {
				// sequenceUtil.sequenceNextVal(sf.name());
				long id = sequenceUtil.sequenceNextVal(obj.getClass());
				ReflectHelper.setValueByFieldName(obj, field.getName(), id);
				return;
			}

		}

	}

	@SuppressWarnings("unused")
	private void setSeqValue(Object obj, String sequenceName) throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException, BaseExcepton {
		Field[] fileds = obj.getClass().getDeclaredFields();
		SequenceUtil sequenceUtil = SequenceUtil.create();
		for (Field field : fileds) {
			long id = sequenceUtil.sequenceNextVal(sequenceName);
			ReflectHelper.setValueByFieldName(obj, field.getName(), id);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
//	public Object plugin(Object target) {
//		return Plugin.wrap(target, this);
//	}
	
	@Override
	 public Object plugin(Object arg0) {    
	        // TODO Auto-generated method stub    
	        if (arg0 instanceof StatementHandler) {    
	            return Plugin.wrap(arg0, this);    
	        } else {    
	            return arg0;    
	        }   
	    } 

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
		saveSqlId = properties.getProperty("saveSqlId");

	}

	public String getSaveSqlId() {
		return saveSqlId;
	}

	public void setSaveSqlId(String saveSqlId) {
		this.saveSqlId = saveSqlId;
	}

	/**
	 * 
	 * @Title: 通过BoundSql截取SQL提取Seq关键字 @param @param sql @param @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	@SuppressWarnings("unused")
	private String matcherSequenceName(String sql) {
		String re = "INSERT INTO(.*)\\(";
		Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuilder sequenceName = new StringBuilder();
		while (m.find()) {
			String patternVal = m.group(1);
			sequenceName.append(patternVal.replaceAll("\\pP|\\pS|\\s", ""));
		}
		return sequenceName.toString();
	}

	public static class BoundSqlSqlSource implements SqlSource {

		private BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}


	public static void main(String[] args) {
		String sql = "INSERT INTO 'test_seq' (ID,NAME)";
		String re = "INSERT INTO(.*)";
		// String re = "INSERT INTO(.*)\\(";
		Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		while (m.find()) {
			System.out.println(m.group(1));
		}
		// String val = sequenceName(sql);
		// System.out.println(val);
	}

}
