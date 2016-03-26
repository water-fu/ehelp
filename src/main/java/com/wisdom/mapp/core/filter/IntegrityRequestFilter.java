package com.wisdom.mapp.core.filter;

import com.wisdom.mapp.core.ErrorCodeDefine;
import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.exception.BusinessException;
import com.wisdom.mapp.core.exception.DefaultExceptionCode;
import com.wisdom.mapp.core.model.IMappDatapackage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IntegrityRequestFilter implements IFilter<Map<String,Object>> {

	Logger logger = LoggerFactory.getLogger(IntegrityRequestFilter.class);
	
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
		
		if(context == null)
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"MAPP 上下文信息为 NULL"));
		
		IMappDatapackage request = (IMappDatapackage)context.get(MappContext.MAPPCONTEXT_REQUEST);
		
		if(request==null)
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"MAPP 请求信息为 NULL"));
		
		if(request.getHeader() == null || StringUtils.isBlank(request.getHeader().getBizCode()))
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"MAPP BIZCODE 为空"));
		
		if(StringUtils.isBlank(request.getHeader().getIdentityId()))
			logger.warn("MAPP 报文流水为空");
		
		logger.info("请求信息："+context.get(MappContext.MAPPCONTEXT_REQUEST_STRING));
		
		return context;
		
	}

}
