package com.foriseland.fjf.thread;

import org.springframework.stereotype.Component;

import com.foriseland.fjf.log.LogMgr;

import javax.annotation.PreDestroy;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @ClassName: com.foriseland.fjf.thread.fkjntask
 * @Description:
 * @author: wangHiayang
 * @date 2016/10/9
 */
@Component
public class ForkJoinFactory {

	private static final int POLL_THREAD_NUM = 8;

	private static ForkJoinPool forkJoinPool;

	private static Boolean isStart = Boolean.FALSE;

	public void init() {
		if (isStart.booleanValue()) return;
		forkJoinPool = new ForkJoinPool(POLL_THREAD_NUM);
		isStart = Boolean.TRUE;
	}

	@PreDestroy
	public void destroyMethod() {
		if (forkJoinPool != null){
			LogMgr.sysInfo("java.util.concurrent ForkJoinPool destroyMethod ");
			forkJoinPool.shutdown();
		}
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	public void execute(ForkJoinTask task) {
		init();
		this.forkJoinPool.execute(task);
	}
}