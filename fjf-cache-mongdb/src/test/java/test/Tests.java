package test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.foriseland.fjf.cache.mongo.BuguConnection;
import com.foriseland.fjf.cache.mongo.BuguFramework;

import test.dao.UserDao;
import test.entity.User;

public class Tests {
	
	public void testInsert() {
		UserDao userDao = new UserDao();
		User bean = new User();
		bean.setUsername("damai");
		bean.setAge(35);
		userDao.insert(bean);
	}
	
	public void testFind() {
		UserDao userDao = new UserDao();
		User  user = userDao.findOne();
		System.out.println(user);
	}
	
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-*.xml");
		//context.wait();
	}
}