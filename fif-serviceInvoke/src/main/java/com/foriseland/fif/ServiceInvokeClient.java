package com.foriseland.fif;

import com.alibaba.druid.pool.DruidDataSource;
import com.foriseland.fif.telnet.ThreadRun;
import com.foriseland.fif.telnet.vo.DruidResult;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ServiceInvokeClient implements InitializingBean, DisposableBean, BeanFactoryAware,ApplicationContextAware {

	private DruidDataSource druidDataSource;

	private DruidResult druidResult = new DruidResult();

	private static ApplicationContext applicationContext = null;


	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ServiceContext.setBeanFactory(beanFactory);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public void init(){
		ThreadRun threadRun = new ThreadRun();
		threadRun.start();
		try {
			druidDataSource = applicationContext.getBean(DruidDataSource.class);
		}catch (Exception e){
			druidDataSource = null;
			System.out.println("当前项目 没有使用德鲁伊数据源");
		}
		System.out.println("启动获取druidDataSource");
	}

	public String getDruidConnectionCount(Integer parent){
		if(druidDataSource != null){
			int activeCount = druidDataSource.getActiveCount();
			int initialSize = druidDataSource.getInitialSize();
			int maxActive = druidDataSource.getMaxActive();
			druidResult.setMaxActive(maxActive);
			druidResult.setInitialSize(initialSize);
			druidResult.setActiveCount(activeCount);
		}else{
			return null;
		}
		return druidResult.toString();
	}


}
