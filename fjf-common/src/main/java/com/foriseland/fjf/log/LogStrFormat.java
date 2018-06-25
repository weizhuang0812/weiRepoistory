package com.foriseland.fjf.log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class LogStrFormat {
	public static String formatOperationLogStr(long memberID, LogStackVO logStackVO, Map<String, Object> params, Object returnValue) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(formatParamsLogStr(logStackVO, params, returnValue));
		logStr.append(" | memberID:");
		logStr.append(memberID);
		return logStr.toString();
	}
	
	public static String formatOperationLogStr(String accountName, LogStackVO logStackVO, Map<String, Object> params, Object returnValue) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(formatParamsLogStr(logStackVO, params, returnValue));
		logStr.append(" | accountName:");
		logStr.append(accountName);
		return logStr.toString();
	}
	
	@SafeVarargs
	public static <T> String formatOperationLogStr(long memberID, LogStackVO logStackVO, T... params) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(formatParamsLogStr(logStackVO, params));
		logStr.append(" | memberID:");
		logStr.append(memberID);
		return logStr.toString();
	}
	
	@SafeVarargs
	public static <T> String formatOperationLogStr(String accountName, LogStackVO logStackVO, T... params) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(formatParamsLogStr(logStackVO, params));
		logStr.append(" | accountName:");
		logStr.append(accountName);
		return logStr.toString();
	}
	
	public static String formatPerformanceLogStr(LogStackVO logStackVO, String className, String methodName, String costTime) {
		StringBuilder logStr = new StringBuilder();
		logStr.append(" ");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(") |");
		logStr.append(" CLASSNAME:");
		logStr.append(className);
		logStr.append(" METHODNAME:");
		logStr.append(methodName);
		logStr.append(" COST_TIME:");
		logStr.append(costTime);
		logStr.append("ms");
		return logStr.toString();
	}
	
	public static String formatParamsLogStr(LogStackVO logStackVO, Map<String, Object> params, Object returnValue) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(" ");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(") |");
		String paramClassName = params.getClass().getName();
		logStr.append(" PARAMCLASS:(CLASSTYPE:");
		logStr.append(paramClassName);
		logStr.append(" PARAMS:{");
		Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
		int n = 1;
		while (iterator.hasNext()) {
			Map.Entry<String, Object> me = iterator.next();
			if (n == 1) {
				logStr.append(me.getKey() + ":" + me.getValue());
			} else {
				logStr.append(",");
				logStr.append(me.getKey() + ":" + me.getValue());
			}
			n++;
		}
		logStr.append("})");
		logStr.append(formatReturnValues(returnValue));
		return logStr.toString();
	}
	
	@SafeVarargs
	public static <T> String formatParamsLogStr(LogStackVO logStackVO, T... params) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		T returnValue = params[params.length - 1];
		StringBuilder logStr = new StringBuilder();
		logStr.append(" ");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(") |");
		for (int i = 0 ; i < params.length - 1 ; i++) {
			T param = params[i];
			logStr.append(formatParamValues(param));
		}
		
		logStr.append(formatReturnValues(returnValue));
		return logStr.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static <T> String formatParamValues(T param) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		if (param == null) {
			logStr.append(" PARAM VALUE:{null})");
		} else {
			String paramClassName = param.getClass().getName();
			logStr.append(" PARAMCLASS:(CLASSTYPE:");
			logStr.append(paramClassName);
			 
			if (paramClassName.indexOf("java.lang.Integer")!=-1 || paramClassName.indexOf("java.lang.Long")!=-1 || paramClassName.indexOf("java.lang.String")!=-1
					|| paramClassName.indexOf("java.lang.Float")!=-1 || paramClassName.indexOf("java.lang.Double")!=-1 || paramClassName.indexOf("java.lang.Short")!=-1
					|| paramClassName.indexOf("java.lang.Number")!=-1) {
				logStr.append(" VALUE:{");
				logStr.append(param);
			} else if (paramClassName.indexOf("java.util.HashMap")!=-1 || paramClassName.indexOf("java.util.HashTable")!=-1 || paramClassName.indexOf("java.util.TreeMap")!=-1) {
				logStr.append(" KEY-VALUES:{");
				Map map = (Map)param;
				Iterator iterator = map.entrySet().iterator();
				int n = 1;
				while (iterator.hasNext()) {
					Map.Entry me = (Map.Entry)iterator.next();
					if (n == 1) {
						logStr.append(me.getKey() + ":" + me.getValue());
					} else {
						logStr.append(",");
						logStr.append(me.getKey() + ":" + me.getValue());
					}
					n++;
				}
			} else {
				int n = 1;
				logStr.append(" PARAMS:{");
				Field[] fields = param.getClass().getDeclaredFields();
				for (Field f : fields) {
					if (f.toGenericString().indexOf("static")!=-1 || f.toGenericString().indexOf("final")!=-1) {
						continue;
					}
					String fieldName = f.getName();
					Class fieldType = f.getType();
					Object obj = null;
					if (fieldType.getName().indexOf("boolean") != -1) {
						obj = invokeISMethod(param, fieldName);
					} else {
						obj = invokeGetMethod(param, fieldName);
					}
					
					if (n > 1) {
						logStr.append(",");
					} 
					logStr.append(fieldName + ":" + obj);
					n++;
				}
			}
			logStr.append("})");
		}
		
		return logStr.toString();
	}
	@SuppressWarnings("unchecked")
	private static <T> String formatReturnValues(T returnValue) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		logStr.append(" | RETURNVALUE:");
		if (returnValue == null) {
			logStr.append(" (VALUE:{null})");
		} else {
			String returnParentClassName = returnValue.getClass().getName();
			if (returnParentClassName.indexOf("java.util.ArrayList")!=-1 || returnParentClassName.indexOf("java.util.LinkedList")!=-1) {
				logStr.append("(CLASSTYPE:");
				logStr.append(returnParentClassName);
				logStr.append(" SUBCLASSES:{");
				List<T> returnValueList = (List<T>)returnValue;
				for (T obj : returnValueList) {
					logStr.append(formatReturnValue(obj));
				}
				logStr.append(")");
			} else if (returnParentClassName.indexOf("java.util.HashSet")!=-1 || returnParentClassName.indexOf("java.util.TreeSet")!=-1) {
				logStr.append("(CLASSTYPE:");
				logStr.append(returnParentClassName);
				logStr.append(" SUBCLASSES:{");
				Set<T> returnValueSet = (Set<T>)returnValue;
				for (T obj : returnValueSet) {
					logStr.append(formatReturnValue(obj));
				}
				logStr.append(")");
			} else {
				logStr.append(formatReturnValue(returnValue));
			}
		}
		return logStr.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static <T> String formatReturnValue(T returnObj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder logStr = new StringBuilder();
		if (returnObj == null) {
			logStr.append(" VALUE:{null}");
		} else {
			String returnClassName = returnObj.getClass().getName();
			logStr.append("(CLASSTYPE:");
			logStr.append(returnClassName);
			if (returnClassName.indexOf("java.lang.Integer")!=-1 || returnClassName.indexOf("java.lang.Long")!=-1 || returnClassName.indexOf("java.lang.String")!=-1
					|| returnClassName.indexOf("java.lang.Float")!=-1 || returnClassName.indexOf("java.lang.Double")!=-1 || returnClassName.indexOf("java.lang.Short")!=-1
					|| returnClassName.indexOf("java.lang.Number")!=-1) {
				logStr.append(" VALUE:{");
				logStr.append(returnObj);
				logStr.append("}");
			} else if (returnClassName.indexOf("java.util.HashMap")!=-1 || returnClassName.indexOf("java.util.HashTable")!=-1 || returnClassName.indexOf("java.util.TreeMap")!=-1) {
				logStr.append(" KEY-VALUES:{");
				Map map = (Map)returnObj;
				Iterator iterator = map.entrySet().iterator();
				int n = 1;
				while (iterator.hasNext()) {
					Map.Entry me = (Map.Entry)iterator.next();
					if (n == 1) {
						logStr.append(me.getKey() + ":" + me.getValue());
					} else {
						logStr.append(",");
						logStr.append(me.getKey() + ":" + me.getValue());
					}
					n++;
				}
				logStr.append("}");
			} else {
				int n = 1;
				logStr.append(" PROPERTIES:{");
				Field[] fields = returnObj.getClass().getDeclaredFields();
				for (Field f : fields) {
					if (f.toGenericString().indexOf("static")!=-1 || f.toGenericString().indexOf("final")!=-1) {
						continue;
					}
					String fieldName = f.getName();
					Class<?> fieldType = f.getType();
					Object obj = null;
					if (fieldType.getName().indexOf("boolean") != -1) {
						obj = invokeISMethod(returnObj, fieldName);
					} else {
						obj = invokeGetMethod(returnObj, fieldName);
					}
					
					if (n > 1) {
						logStr.append(",");
					} 
					logStr.append(fieldName + ":" + obj);
					n++;
				}
				logStr.append("}");
			}
			logStr.append(")");
		}
		return logStr.toString();
	}
	
	private static Object invokeGetMethod(Object owner, String attrName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String methodName = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
		Class<?> cla = owner.getClass();
		Class<?> cls[] = {};
		Object[] args = {};
		Method md = cla.getMethod(methodName, cls);
		Object result = md.invoke(owner, args);
		return result;
	}
	
	private static Object invokeISMethod(Object owner, String attrName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String methodName = "is" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
		Class<?> cla = owner.getClass();
		Class<?> cls[] = {};
		Object[] args = {};
		Method md = cla.getMethod(methodName, cls);
		Object result = md.invoke(owner, args);
		return result;
	}
	
	public static String formatErrorLogStr(LogStackVO logStackVO,  String desc) {
		StringBuilder logStr = new StringBuilder();
		logStr.append("classDescribe:{");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
//		logStr.append(") | ");
		logStr.append(")} || ");
		if (desc != null && !desc.equals("")) {
			logStr.append("infoValues:{");
			logStr.append(desc);
//			logStr.append(" | ");
		}
		logStr.append("}");
//		StringBuilder logStr = new StringBuilder();
//		logStr.append("fileName:");
//		
//		logStr.append("(");
//		logStr.append(logStackVO.getFileName());
//		logStr.append(":");
//		logStr.append(logStackVO.getLineNumber());
//		logStr.append(") |*|");
//		logStr.append("info:").append(desc);
		System.out.println(logStr);
		return logStr.toString();
	}
	
	public static String formatErrorLogStr(LogStackVO logStackVO,  Object desc) {
		StringBuilder logStr = new StringBuilder();
		logStr.append("classDescribe:{");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(")} || ");
		if (desc != null && !desc.equals("")) {
			logStr.append("infoValues:{");
			logStr.append(desc);
		}
		logStr.append("}");
		System.out.println(logStr);
		return logStr.toString();
	}
	
	@SafeVarargs
	public static <T> String formatTradeLogStr(LogStackVO logStackVO, String desc, T... params) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		T returnValue = params[params.length - 1];
		StringBuilder logStr = new StringBuilder();
		logStr.append(" ");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(") | ");
		logStr.append(desc);
		logStr.append("\n\t");
		logStr.append(" | ");
		for (int i = 0 ; i < params.length - 1 ; i++) {
			T param = params[i];
			logStr.append(formatParamValues(param));
			logStr.append("\n\t");
		}
		logStr.append(formatReturnValues(returnValue));
		logStr.append("\n\t");
		logStr.append("************************************************************************************************************");
		return logStr.toString();
	}
	
	
	public static <T> String formatParamsLogMessage(LogStackVO logStackVO, String message){
		StringBuilder logStr = new StringBuilder();
		logStr.append(" ");
		logStr.append(logStackVO.getClassName());
		logStr.append(".");
		logStr.append(logStackVO.getMethodName());
		logStr.append("(");
		logStr.append(logStackVO.getFileName());
		logStr.append(":");
		logStr.append(logStackVO.getLineNumber());
		logStr.append(") | ");
		logStr.append(message);
		return logStr.toString();
	}
}
