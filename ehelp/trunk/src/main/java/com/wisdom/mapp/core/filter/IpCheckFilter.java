package com.wisdom.mapp.core.filter;

import com.wisdom.mapp.core.ActionConfig;
import com.wisdom.mapp.core.ApplicationConfig;
import com.wisdom.mapp.core.ErrorCodeDefine;
import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.configuration.MappSettings;
import com.wisdom.mapp.core.exception.BusinessException;
import com.wisdom.mapp.core.exception.DefaultExceptionCode;
import com.wisdom.mapp.core.model.IMappDatapackage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class IpCheckFilter implements IFilter<Map<String,Object>> {

	Logger logger = LoggerFactory.getLogger(RightCheckFilter.class);
	
	@Autowired
	private MappSettings mappSettings;
	
	private static String IP_SPLIT_CHAR = ",";
	
	private Set<String> valid_IP_Stack;
	
	@Override
	@PostConstruct
	public void init() throws Exception {
		try
		{
			if(StringUtils.isBlank(mappSettings.getValidIp()) == false)
			{
				valid_IP_Stack = new HashSet<String>(0);
				
				String[] ip_list = mappSettings.getValidIp().split(IP_SPLIT_CHAR);
				for(String ip : ip_list)
				{
					if(StringUtils.isBlank(ip))
						continue;
					
					valid_IP_Stack.add(ip);
				}
//				valid_IP_Stack.addAll(Arrays.asList(ip_list));
			}
		}catch (Exception e) {
			logger.error("初始化载入 VALID_IP 出错。");
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR, "初始化载入validIP出错。"), e);
		}
		
		logger.debug("VALID_IP 初始化完成");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String,Object> doFilter(Map<String,Object> context) throws Exception {
		
		String requestIP = (String) context.get(MappContext.MAPPCONTEXT_REQUEST_IP);
		
		logger.debug("请求IP:"+requestIP);
		
		IMappDatapackage request = (IMappDatapackage)context.get(MappContext.MAPPCONTEXT_REQUEST);
		
		ActionConfig config = ApplicationConfig.getActionConfig(request.getHeader().getBizCode());
		
		if(config == null)
			return context;
		
		if(config.isIpCheck() == false || valid_IP_Stack == null || valid_IP_Stack.isEmpty())
			return context;
		
		if(valid_IP_Stack.contains("*")) {
			
		} else {
			if(valid_IP_Stack.contains(requestIP) == false)
				throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"未被允许的IP请求，IP:"+requestIP));
		}
		return context;

	}

}
