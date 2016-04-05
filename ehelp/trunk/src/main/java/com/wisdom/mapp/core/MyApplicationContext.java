/**
 * 
 */
package com.wisdom.mapp.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**

 */
@Component("factory")
public class MyApplicationContext implements ApplicationContextAware {
	
	private ApplicationContext context;
	
	public<T> T getBean(String key) {
		if(this.context == null) {
			throw new RuntimeException("spring context 没有初始化");
		}
		
		return (T)context.getBean(key);
	}
	
	public<T> T getBean(Class<T> clazz) {
		if(this.context == null) {
			throw new RuntimeException("spring context 没有初始化");
		}
		return context.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		this.context=contex; 
		
	}
	
	

}
