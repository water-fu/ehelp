package com.wisdom.mapp.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.mapp.core.exception.MappException;
import com.wisdom.mapp.core.exception.MappExceptionCode;
import com.wisdom.mapp.core.model.IMappDatapackage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("jsonHandler")
public class JsonClientHandler<T extends IMappDatapackage> extends StringClientHandler<T>{

	Logger logger = LoggerFactory.getLogger(JsonClientHandler.class);
	
	@Override
	protected String buildResponse(T response) throws MappException
	{
		String json = null;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			
			json=mapper.writeValueAsString(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new MappException(MappExceptionCode.UNRECOGNIZED_RESPONSE,e);
		}
	
		logger.debug("Json response ："+json);
		
		return json;
		
	}

	@Override
	protected T preProcess(String request) throws MappException {
		
		logger.debug("Json request："+request);
		
		if(StringUtils.isBlank(request))
			return null;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			return (T)mapper.readValue(request, IMappDatapackage.class);
		} catch (Exception e) {
			throw new MappException(MappExceptionCode.UNRECOGNIZED_REQUEST, e);
		} 
	}

}
