package com.foriseland.fjf.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 建议 日志key 包括不限于 path(方法完全路径:包名-方法名) para(参数key=value,多个用逗号分隔) case
 * reason(case原因) result(返回值,可选) request(对外接口时请求来源,如IP,可选) cost(时间,可选)
 **/
public class LogMgr {

	// 异常日志
	static Logger errorAppender = LoggerFactory.getLogger("errorAppender");

	// 系统信息日志
	static Logger infoAppender = LoggerFactory.getLogger("infoAppender");
	
	static Logger debugAppender = LoggerFactory.getLogger("debugAppender");
	
	// 操作日志
	static Logger operatorAppender = LoggerFactory.getLogger("operatorAppender");

	/*
	 * 
	 * public static void writeDBLog(){ 可以通过自定义log event的方式,暂时不用 }
	 */
	
	
	/**
	 * @title: operatorLog 
	 * @describe:操作日志
	 * @param logs
	 * @author: wangHaiyang
	 * @createTime 2018年3月19日 下午4:38:24 
	 * @throws
	 */
	public static void operatorLog(String logs) {
		if (operatorAppender.isInfoEnabled())    {
			operatorAppender.info(logs);
		}
	}

	public static void sysInfo(String logStr) {
		// String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		// StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		// String callName = stack[2].getClassName();
		// String callMethodName = stack[2].getMethodName();
		// int lineNumber = stack[2].getLineNumber();
		// String fileName = stack[2].getFileName();
		// StringBuilder appends = new StringBuilder();
		// appends.append("fileName:").append(fileName).append(" | ").
		// append("className:").append(callName).append(" |
		// ").append("methodName:").append(callMethodName).append(" | ").
		// append("lineNumber:").append(lineNumber).append(" | ")
		// .append("info:").append(logStr);
		if (infoAppender.isInfoEnabled())    {
			LogStackVO logStackVO = getCurrentThreadStackTrace();
			infoAppender.info(LogStrFormat.formatErrorLogStr(logStackVO, logStr));
		}
	}
	
	public static void sysInfo(Object logStr) {
		if (infoAppender.isInfoEnabled())    {
			LogStackVO logStackVO = getCurrentThreadStackTrace();
			infoAppender.info(LogStrFormat.formatErrorLogStr(logStackVO, logStr));
		}
	}

	/**
	 * @Title: sysInfoLog @Description: 多个日志信息拼接，采用字符串逗号拼接方式 @return void
	 *         返回类型 @author: wangHaiyang @throws
	 */
	public static void sysInfo(String... logStr) {
		if (infoAppender.isInfoEnabled())    {
			int size = null == logStr ? 0 : logStr.length;
			StringBuilder appends = new StringBuilder(size);
			for (String item : logStr) {
				appends.append(item);
			}
			infoAppender.info(appends.toString());
		}
	}
	
	public static void sysInfo(Object... logStr) {
		if (infoAppender.isInfoEnabled())    {
			int size = null == logStr ? 0 : logStr.length;
			StringBuilder appends = new StringBuilder(size);
			for (Object item : logStr) {
				appends.append(item);
			}
			infoAppender.info(appends.toString());
		}
	}
	
	public void debug(String info) {    
        if (debugAppender.isDebugEnabled())    
        	debugAppender.debug(info);    
    }
	
	public void debug(Object info) {    
        if (debugAppender.isDebugEnabled())    
        	debugAppender.debug(String.valueOf(info));    
    }

	/**
	 * @Title: writeErrorLog @Description:异常日志 @param @param msg @param @param e
	 *         设定文件 @return void 返回类型 @author: admin @throws
	 */
	public static void error(String msg, Throwable e) {
		LogStackVO logStackVO = getCurrentThreadStackTrace();
		errorAppender.error(LogStrFormat.formatErrorLogStr(logStackVO, msg), e);
	}

	public static void error(String msg) {
		LogStackVO logStackVO = getCurrentThreadStackTrace();
		errorAppender.error(LogStrFormat.formatErrorLogStr(logStackVO, msg), "");
	}
	
	public static void error(Object msg, Throwable e) {
		LogStackVO logStackVO = getCurrentThreadStackTrace();
		errorAppender.error(LogStrFormat.formatErrorLogStr(logStackVO, msg), e);
	}

	public static void error(Object msg) {
		LogStackVO logStackVO = getCurrentThreadStackTrace();
		errorAppender.error(LogStrFormat.formatErrorLogStr(logStackVO, msg), "");
	}


	private static LogStackVO getCurrentThreadStackTrace() {
		LogStackVO logStackVO = new LogStackVO();
		StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
		String className = ste.getClassName();
		String methodName = ste.getMethodName();
		int lineNumber = ste.getLineNumber();
		String fileName = ste.getFileName();
		logStackVO.setClassName(className);
		logStackVO.setMethodName(methodName);
		logStackVO.setLineNumber(lineNumber);
		logStackVO.setFileName(fileName);
		return logStackVO;
	}

	public static void main(String[] args) {
		/*
		 * // writeOperationLog(128, new HashMap(), new HashMap()); SequenceModel
		 * sequenceModel = new SequenceModel(); sequenceModel.setSeqCurrentVal(12345);
		 * sequenceModel.setSeqName("wang"); LogStackVO logStackVO = new LogStackVO();
		 * logStackVO.setFileName("11.txt"); logStackVO.setLineNumber(121);
		 * logStackVO.setMethodName("dubbo!"); // logStackVO.setVaild(true); //
		 * logStackVO.setInvalid(new Boolean(false)); // logStackVO.setYy(123L);
		 * Map<String, Object> map = new HashMap<String, Object>(); map.put("abc",
		 * "1234"); map.put("WK", "wk99114"); map.put("logStackVO", logStackVO);
		 * Set<Object> set = new HashSet<Object>(); List apc = new ArrayList();
		 * Map<String, Object> map2 = new HashMap<String, Object>(); map2.put("bbc",
		 * "1234"); map2.put("name", "wjj"); apc.add(map); apc.add(map2); //
		 * writeOperationLog(127, sequenceModel, logStackVO, "wo", apc); //
		 * writeDebugLog(new Long(12), "555"); // Long testIdLong=1111L; // long siteId
		 * = 123; // LogMgr.writeDebugLog("siteId: " +siteId,testIdLong,"");
		 * TradeLogMgr.writeSuccessLog("第19笔交易成功", logStackVO, apc); //
		 * TradeLogMgr.writeFailedLog("第9笔交易失败", new RuntimeException(), logStackVO,
		 * apc);
		 */

		sysInfo("1", "2");
		sysInfo("1", "2");
	}
}
