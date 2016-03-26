package com.wisdom.mapp.core.filter;

import com.wisdom.mapp.core.ActionConfig;
import com.wisdom.mapp.core.ApplicationConfig;
import com.wisdom.mapp.core.ErrorCodeDefine;
import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.exception.BusinessException;
import com.wisdom.mapp.core.exception.DefaultExceptionCode;
import com.wisdom.mapp.core.model.IMappDatapackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RightCheckFilter implements IFilter<Map<String,Object>> {

	Logger logger = LoggerFactory.getLogger(RightCheckFilter.class);
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String,Object> doFilter(Map<String,Object> context) throws Exception {
		
		IMappDatapackage request = (IMappDatapackage)context.get(MappContext.MAPPCONTEXT_REQUEST);
		
		ActionConfig config = ApplicationConfig.getActionConfig(request.getHeader().getBizCode());
		
		if(config == null)
			return context;
		
		if(config.isRightCheck() == false)
			return context;
		
		Set<String> rights = (Set<String>) context.get(MappContext.MAPPCONTEXT_RIGHT);
		
		if(rights == null || rights.contains(MappContext.getRequest().getHeader().getBizCode()) == false)
		{
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"用户未被允许请求该接口("+MappContext.getRequest().getHeader().getBizCode()+")"));
		}

		logger.debug("权限："+rights);
		
		return context;
	}

}
