package com.foriseland.fif;

import org.springframework.beans.factory.BeanFactory;

public class ServiceContext {

	private static BeanFactory beanFactory;

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static void setBeanFactory(BeanFactory beanFactory) {
		ServiceContext.beanFactory = beanFactory;
	}
	

}
