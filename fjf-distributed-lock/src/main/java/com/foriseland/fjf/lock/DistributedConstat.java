package com.foriseland.fjf.lock;

public class DistributedConstat {
	public static final String RAtomicName = "genId_";

	protected final static String LOCK_SUFFIX = "DISTRIBUTED&LOCK@";
	protected final static String LOG_PREFIX = "[#Foriseland-Distributed-Lock#] ->";
	protected final static int REDI_SIMPLE_AUTO_TIME = 30; // 支持过期解锁功能,30秒钟以后自动解锁
	protected final static int REDI_WAIT_TIME = 10; // 最多等待10秒，上锁以后10秒自动解锁

	
}
