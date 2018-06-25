//package com.foriseland.fjf.log;
//
//import org.apache.log4j.Logger;
//
//public class CacheLogMgr {
//	//cache异常日志
//    static final Logger cacheErrorLogger = Logger.getLogger("cacheError");
//    
//    public static void writeErrorLog(Throwable e) {
// 	   	LogStackVO logStackVO = getCurrentThreadStackTrace();
// 	   	cacheErrorLogger.error(LogStrFormat.formatErrorLogStr(logStackVO, null), e);
//    }
//    
//    public static void writeErrorLog(String msg, Throwable e) {
// 	   	LogStackVO logStackVO = getCurrentThreadStackTrace();
// 	   	cacheErrorLogger.error(LogStrFormat.formatErrorLogStr(logStackVO, msg), e);
//    }
//    
//    private static LogStackVO getCurrentThreadStackTrace() {
// 	   	LogStackVO logStackVO = new LogStackVO();
// 	   	StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
// 	   	String className = ste.getClassName();
// 	   	String methodName = ste.getMethodName();
// 	   	int lineNumber = ste.getLineNumber();
// 	   	String fileName = ste.getFileName();
// 	   	logStackVO.setClassName(className);
// 	   	logStackVO.setMethodName(methodName);
// 	   	logStackVO.setLineNumber(lineNumber);
// 	   	logStackVO.setFileName(fileName);
// 	   	return logStackVO;
//    }
//}
