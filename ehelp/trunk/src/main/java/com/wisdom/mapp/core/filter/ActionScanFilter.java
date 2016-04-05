package com.wisdom.mapp.core.filter;

import com.wisdom.mapp.core.ActionConfig;
import com.wisdom.mapp.core.Application;
import com.wisdom.mapp.core.ApplicationConfig;
import com.wisdom.mapp.core.annotation.Action;
import com.wisdom.mapp.core.annotation.Request;
import com.wisdom.mapp.core.annotation.Response;
import com.wisdom.mapp.core.configuration.MappSettings;
import com.wisdom.mapp.core.util.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 */
@Component
@Lazy(value=false)
public class ActionScanFilter implements IFilter<Object> {
	
	@Autowired
	private MappSettings mappSettings;
//	
//	@Value("${action.package}")
//	private String actionPackage;
	
	@PostConstruct 
	public void init() 
	{	
//		actionPackage = "com.ailk.yd.mapp.client.action";
		System.out.println("======================ActionScanFilter actionPackage:"+mappSettings.getActionPackage()+"========================"+ Application.getValue("action.package"));
		Set<Class<?>> types = new HashSet<Class<?>>(0);
		String[] packages = mappSettings.getActionPackage().split(",");
		
		for(String p : packages)
			types.addAll(ClassUtils.getClasses(p));
//		Set<Class<?>> types=ClassUtils.getClasses(this.getActionPackage());
		
    	for(Class type:types) {
    		
    		Action action= AnnotationUtils.findAnnotation(type, com.wisdom.mapp.core.annotation.Action.class);
    		if(action != null) {
    			String bizcode= AnnotationUtils.getValue(action, "bizcode").toString();
    			Boolean userCheck=(Boolean) AnnotationUtils.getValue(action, "userCheck");
    			Boolean rightCheck=(Boolean) AnnotationUtils.getValue(action, "rightCheck");
    			Boolean ipCheck=(Boolean) AnnotationUtils.getValue(action, "ipCheck");
    			Field[] fields=type.getDeclaredFields();
    			Class requestType=null;
    			Class responseType= null;
    			for(Field field:fields) {
    				Request request=field.getAnnotation(Request.class);
    				if(request != null) {
    					//requestType=field.getDeclaringClass();
    					requestType=field.getType();
    				}
    				
    				Response response=field.getAnnotation(Response.class);
    				if(response != null) {
    					responseType=field.getType();
    				}
    			}
    			ActionConfig config=new ActionConfig(bizcode, type, requestType, responseType);
    			config.setUserCheck(userCheck);
    			config.setIpCheck(ipCheck);
    			config.setRightCheck(rightCheck);
    			ApplicationConfig.addActionConfig(bizcode, config);
    		}
    	}
	}

	/* (non-Javadoc)
	 * @see com.ailk.uni.core.IFilter#destroy()
	 */
	public void destroy() {
		return;
	}

	@Override
	public Object doFilter(Object context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
