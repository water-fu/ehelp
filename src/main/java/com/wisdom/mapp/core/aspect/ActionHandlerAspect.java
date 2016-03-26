package com.wisdom.mapp.core.aspect;

import com.wisdom.mapp.core.ErrorCodeDefine;
import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.exception.BusinessException;
import com.wisdom.mapp.core.exception.DefaultExceptionCode;
import com.wisdom.mapp.core.exception.SystemException;
import com.wisdom.mapp.core.filter.FilterChain;
import com.wisdom.mapp.core.filter.FilterChainAware;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 实现带filter的切面定义Aspect模块
 *
 */
@Aspect
@Component
public class ActionHandlerAspect extends FilterChainAware<Map<String,Object>> {
	
	Logger logger = LoggerFactory.getLogger(ActionHandlerAspect.class);
	
	@Autowired
	public void setFilterChain(@Qualifier("handlerFilterChain") FilterChain<Map<String,Object>> filterChain) {
		this.filterChain = filterChain;
	}
	
	@Autowired
	public void setCustomFilterChain(FilterChain<Map<String, Object>> customFilterChain) {
		this.customFilterChain = customFilterChain;
	}
	
	/**
	 * 拦截处理actionHandle方法，在此方法前执行已经定义的filter
	 * @param point
	 * @throws Throwable 
	 */
	@Around("execution(* com.wisdom.mapp.core.base.BaseActionHandler.doHandle(..))")
	public void AroundActionHandler(ProceedingJoinPoint point) throws Throwable
	{
		try{
			
//			logger.debug("请求对象" + (MappContext.getRequestString() == null ? null : MappContext.getRequestString().toString()));
			
			logger.debug("进入拦截器 ActionHandlerAspect.beforeActionHandler。");
			
			if(filterChain == null)
			{
				logger.debug("过滤器链为空，结束 ActionHandlerAspect.AroundServerHandler 拦截。");
				return;
			}
			
			/**
			 * 过滤器
			 */
			Map<String, Object> context = MappContext.getContext();
			
			if(filterChain != null)
				context = filterChain.doFilterChain(MappContext.getContext());
			if(customFilterChain != null)
				context = customFilterChain.doFilterChain(context);
			
			MappContext.initContext(context);
			
			logger.debug("结束 ActionHandlerAspect.AroundServerHandler 拦截。");
			
			point.proceed();
			
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.error(e.getErrorCode()+":"+e.getErrorMsg());
			MappContext.getResponse().getHeader().setRespCode(e.getErrorCode());
			MappContext.getResponse().getHeader().setRespMsg(e.getErrorMsg());
	
		} catch (SystemException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			MappContext.getResponse().getHeader().setRespCode(e.getErrorCode());
			MappContext.getResponse().getHeader().setRespMsg(e.getErrorMsg());
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			MappContext.getResponse().getHeader().setRespCode(ErrorCodeDefine.UNKNOW_ERROR);
			MappContext.getResponse().getHeader().setRespMsg(e.getMessage());
	
		}
		finally
		{
			filterChain.destroyFilterChain();
		}
	}
	
	@AfterReturning(value="execution(* com.wisdom.mapp.core.base.BaseActionHandler.doHandle(..))")
	public void afterActionHandler() throws Exception
	{
		logger.debug("返回报文检测开始");
		
		if(MappContext.getContext() == null)
//			throw new BusinessException(ErrorCodeDefine.EXPECT_ERROR,"MAPP上下文信息为 NULL");
			throw new BusinessException(new DefaultExceptionCode("9999","MAPP上下文信息为 NULL"));
		
		if(MappContext.getResponse()==null)
//			throw new BusinessException(ErrorCodeDefine.EXPECT_ERROR,"MAPP上下文中响应信息(Response)为 NULL");
			throw new BusinessException(new DefaultExceptionCode("9999","MAPP上下文中响应信息(Response)为 NULL"));
		
		if(MappContext.getResponse().getHeader() == null 
				|| StringUtils.isBlank(MappContext.getRequest().getHeader().getBizCode()))
//			throw new BusinessException(ErrorCodeDefine.EXPECT_ERROR,"MAPP BizCode 为空");
			throw new BusinessException(new DefaultExceptionCode("9999","MAPP BizCode 为空"));
		
		if(StringUtils.isBlank(MappContext.getResponse().getHeader().getIdentityId()))
			logger.warn("MAPP 报文流水(IdentityId)为空");
		
		if(StringUtils.isBlank(MappContext.getResponse().getHeader().getRespCode()))
			logger.warn("MAPP 响应编码(RespCode)为空");
		
		logger.debug("返回报文检测结束");
		
	}

}
