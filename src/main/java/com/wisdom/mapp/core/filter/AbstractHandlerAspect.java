package com.wisdom.mapp.core.filter;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 实现带filter的切面定义Aspect模块
 */
@Aspect
@Component
public class AbstractHandlerAspect<T> extends FilterChainAware<T> {

	@Override
	public void setFilterChain(FilterChain<T> filterChain) {
		
		
	}
//	
//	Logger logger = LoggerFactory.getLogger(ActionHandlerAspect.class);
//	
//	@Override
//	@Autowired
//	public void setFilterChain(@Qualifier("handlerFilterChain") FilterChain<MappContext> filterChain) {
//		this.filterChain = filterChain;
//	}
//	
//	/**
//	 * 拦截处理actionHandle方法，在此方法前执行已经定义的filter
//	 * @param context
//	 * @throws Throwable 
//	 */
//	@Around("execution(* com.ailk.butterfly.mapp.core.service.BaseActionHandler.doHandle(..)) && args(context)")
//	public MappContext AroundActionHandler(ProceedingJoinPoint point,MappContext context) throws Throwable 
//	{
//		logger.debug("进入拦截器 ActionHandlerAspect.beforeActionHandler。");
//		
//		if(filterChain == null)
//		{
//			logger.debug("过滤器链为空，结束 ActionHandlerAspect.AroundServerHandler 拦截。");
//			return context;
//		}
//		
//		context = filterChain.doFilterChain(context);
//				
//		logger.debug("结束 ActionHandlerAspect.AroundServerHandler 拦截。");
//		
//		Object retValue = point.proceed(new Object[]{context});
//		
//		return(MappContext)retValue;
//				
//	}
//	
//	@AfterReturning(value="execution(* com.ailk.butterfly.mapp.core.service.BaseActionHandler.doHandle(..))",returning="context")
//	public void afterReturningActionHandler(MappContext context) throws Exception
//	{
//		logger.debug("返回报文检测开始");
//		
//		if(context == null)
//			throw new Exception("MAPP上下文信息为 NULL");
//		
//		if(context.getResponse()==null)
//			throw new Exception("MAPP上下文中响应信息(Response)为 NULL");
//		
//		if(context.getResponse().getHeader() == null 
//				|| StringUtils.isBlank(context.getRequest().getHeader().getBizCode()))
//			throw new Exception("MAPP BizCode 为空");
//		
//		if(StringUtils.isBlank(context.getResponse().getHeader().getIdentityId()))
//			logger.warn("MAPP 报文流水(IdentityId)为空");
//		
//		if(StringUtils.isBlank(context.getResponse().getHeader().getRespCode()))
//			logger.warn("MAPP 响应编码(RespCode)为空");
//		
//		logger.debug("返回报文检测结束");
//		
//	}
//	
//	@After(value="execution(* com.ailk.butterfly.mapp.core.service.BaseActionHandler.doHandle(..))")
//	public void afterActionHandler() throws Exception
//	{
//		filterChain.destroyFilterChain();
//	}
//	
//	@AfterThrowing(value="execution(* com.ailk.butterfly.mapp.core.service.BaseActionHandler.doHandle(..))")
//	public void AfterThrowing() throws Exception
//	{
//		filterChain.destroyFilterChain();
//	}

	@Override
	public void setCustomFilterChain(FilterChain<T> customFilterChain) {
		// TODO Auto-generated method stub
		
	}

	

}
