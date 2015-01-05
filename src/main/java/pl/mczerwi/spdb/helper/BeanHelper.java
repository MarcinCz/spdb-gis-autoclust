package pl.mczerwi.spdb.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BeanHelper {
	
	private ApplicationContext ctx;
	private static BeanHelper INSTANCE;
	
	BeanHelper() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public static BeanHelper getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new BeanHelper();
		}
		
		return INSTANCE;
	}
	public void initBean(Object bean) {
		ctx.getAutowireCapableBeanFactory().autowireBean(bean);
	}
}
