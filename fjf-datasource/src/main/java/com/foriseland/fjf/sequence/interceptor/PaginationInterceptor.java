//package com.foriseland.fjf.sequence.interceptor;
//
//import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.ibatis.executor.ErrorContext;
//import org.apache.ibatis.executor.ExecutorException;
//import org.apache.ibatis.executor.statement.BaseStatementHandler;
//import org.apache.ibatis.executor.statement.RoutingStatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.ParameterMapping;
//import org.apache.ibatis.mapping.ParameterMode;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Plugin;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.property.PropertyTokenizer;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.type.TypeHandler;
//import org.apache.ibatis.type.TypeHandlerRegistry;
//
//import com.foriseland.fjf.exception.BaseExcepton;
//import com.foriseland.fjf.sequence.annotation.SequenceField;
//import com.foriseland.fjf.sequence.handle.SequenceUtil;
//import com.foriseland.fjf.sequence.util.ReflectHelper;
//import com.foriseland.fjf.util.Pager;
//
//import ch.qos.logback.core.util.ContextUtil;
//
//@Intercepts({
//		@org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.statement.StatementHandler.class, method = "prepare", args = {
//				Connection.class }) })
//public class PaginationInterceptor implements Interceptor {
//
//	private String pageSqlId = "";
//	private String saveSqlId = "";
//
//	public String getPageSqlId() {
//		return this.pageSqlId;
//	}
//
//	public void setPageSqlId(String pageSqlId) {
//		this.pageSqlId = pageSqlId;
//	}
//
//	private String getCountQuery(String hql) throws Exception {
//		String hql1 = hql.replaceAll("\n|\t|\r", " ");
//		Matcher m = Pattern.compile("\\s+from\\s+.+", 2).matcher(hql1);
//		if (m.find()) {
//			String subSql = m.group();
//			m = Pattern.compile("\\s+order\\s+by\\s+", 2).matcher(subSql);
//			if (m.find()) {
//				subSql = subSql.substring(0, subSql.indexOf(m.group()));
//			}
//			return "select count(*)" + subSql;
//		}
//		throw new Exception("无效的查询语句【" + hql + "】");
//	}
//
//	private int getCustomerSetCount() {
////		Object o = ContextUtil.getRequest().getAttribute("pager");
////		int count = -1;
////		if (o != null) {
////			count = ((Pager) o).getTotal();
////		}
////		return count;
//		return 10;
//	}
//
//	@Override
//	public Object intercept(Invocation ivk) throws Throwable {
//		if ((ivk.getTarget() instanceof RoutingStatementHandler)) {
//			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
//			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,"delegate");
//			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,"mappedStatement");
//			Connection connection;
//			String countSql;
//			if (mappedStatement.getId().matches(this.pageSqlId)) {
//				BoundSql boundSql = delegate.getBoundSql();
//				Pager pager = ContextUtil.getPager();
//				if (pager == null) {
//					System.err.println("pager分页参数传入不正确！");
//					throw new NullPointerException("分页参数传入不正确！");
//				}
//				int count = getCustomerSetCount();
//				if (count == -1) {
//					Object parameterObject = boundSql.getParameterObject();
//					connection = (Connection) ivk.getArgs()[0];
//					String sql = boundSql.getSql();
//					countSql = getCountQuery(sql);
//					System.err.println("分页sql，查询总记录数:(countSql=" + countSql + ")");
//					PreparedStatement countStmt = connection.prepareStatement(countSql);
//					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,boundSql.getParameterMappings(), parameterObject);
//					setParameters(countStmt, mappedStatement, countBS, parameterObject);
//					ResultSet rs = countStmt.executeQuery();
//					if (rs.next()) {
//						count = rs.getInt(1);
//					}
//					rs.close();
//					countStmt.close();
//				}
//				pager.setTotal(count);
//				ContextUtil.setRequestAttribute("pager", pager);
//
//				ReflectHelper.setValueByFieldName(boundSql, "sql", generatePageSql(boundSql.getSql(), pager));
//			}
//			if (mappedStatement.getId().matches(this.saveSqlId)) {
//				BoundSql boundSql = delegate.getBoundSql();
//				System.err.println("分页sql，查询数据:(sql=" + boundSql.getSql() + ")");
//				Object parameterObject = boundSql.getParameterObject();
//				if ((parameterObject instanceof Map)) {
//					Collection<?> col = ((Map<?,?>) parameterObject).values();
//					for (Object obj : col) {
//						if ((obj instanceof Collection)) {
//							for (Object obj1 : (Collection) obj) {
//								setSeqValue(obj1);
//							}
//						} else {
//							setSeqValue(obj);
//						}
//					}
//				} else {
//					setSeqValue(parameterObject);
//				}
//			}
//		}
//		return ivk.proceed();
//	}
//
//	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
//			Object parameterObject) throws SQLException {
//		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
//		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//		if (parameterMappings != null) {
//			Configuration configuration = mappedStatement.getConfiguration();
//			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
//			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
//			for (int i = 0; i < parameterMappings.size(); i++) {
//				ParameterMapping parameterMapping = (ParameterMapping) parameterMappings.get(i);
//				if (parameterMapping.getMode() != ParameterMode.OUT) {
//					String propertyName = parameterMapping.getProperty();
//					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
//					Object value = null;
//					if (parameterObject == null) {
//						value = null;
//					} else {
//						if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
//							value = parameterObject;
//						} else {
//							if (boundSql.hasAdditionalParameter(propertyName)) {
//								value = boundSql.getAdditionalParameter(propertyName);
//							} else if ((propertyName.startsWith("__frch_"))
//									&& (boundSql.hasAdditionalParameter(prop.getName()))) {
//								value = boundSql.getAdditionalParameter(prop.getName());
//								if (value != null) {
//									value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
//								}
//							} else {
//								value = metaObject == null ? null : metaObject.getValue(propertyName);
//							}
//						}
//					}
//					TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
//					if (typeHandler == null) {
//						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
//								+ " of statement " + mappedStatement.getId());
//					}
//					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
//				}
//			}
//		}
//	}
//
//	private String generatePageSql(String sql, Pager pager) {
//		if (pager != null) {
//			StringBuffer pageSql = new StringBuffer();
//			pageSql.append(sql);
//			pageSql.append(" limit " + pager.getStart() + "," + pager.getPageSize());
//			return pageSql.toString();
//		}
//		return sql;
//	}
//
//	public Object plugin(Object arg0) {
//		return Plugin.wrap(arg0, this);
//	}
//
//	public void setProperties(Properties p) {
//		this.pageSqlId = p.getProperty("pageSqlId");
//	}
//
//	private void setSeqValue(Object obj) throws SecurityException, IllegalArgumentException, NoSuchFieldException,
//			IllegalAccessException, BaseExcepton {
//		Field[] fileds = obj.getClass().getDeclaredFields();
//		for (Field field : fileds) {
//			SequenceField sf = (SequenceField) field.getAnnotation(SequenceField.class);
//			if (sf != null) {
//				ReflectHelper.setValueByFieldName(obj, field.getName(),SequenceUtil.create().sequenceNextVal(obj.getClass()));
//				return;
//			}
//		}
//	}
//
//	public String getSaveSqlId() {
//		return this.saveSqlId;
//	}
//
//	public void setSaveSqlId(String saveSqlId) {
//		this.saveSqlId = saveSqlId;
//	}
//
//}
