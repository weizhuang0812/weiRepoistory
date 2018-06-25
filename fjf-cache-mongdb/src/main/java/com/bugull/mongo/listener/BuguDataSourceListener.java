package com.bugull.mongo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.foriseland.fjf.cache.mongo.BuguConnection;
import com.foriseland.fjf.cache.mongo.BuguFramework;

public class BuguDataSourceListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		BuguConnection conn = BuguFramework.getInstance().createConnection();
		conn.connect("192.168.0.100", 27017, "test", "test", "test");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		BuguFramework.getInstance().destroy();

	}

}
