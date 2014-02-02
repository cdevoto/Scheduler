package com.compuware.ruxit.synthetic.test.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextService {
    private ApplicationContext context;
	
	private static class InstanceHolder {
		private static final AppContextService INSTANCE = new AppContextService();
	}
	
	public static ApplicationContext getApplicationContext () {
		return InstanceHolder.INSTANCE.getContext();
	}
	
	private AppContextService () {
		this.context = new ClassPathXmlApplicationContext("app-context.xml");
	}
	
	private ApplicationContext getContext () {
		return this.context;
	}

}
