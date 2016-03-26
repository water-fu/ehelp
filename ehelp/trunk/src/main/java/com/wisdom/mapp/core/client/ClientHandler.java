package com.wisdom.mapp.core.client;

import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.MyApplicationContext;
import com.wisdom.mapp.core.exception.MappException;
import com.wisdom.mapp.core.exception.MappExceptionCode;
import com.wisdom.mapp.core.interfaces.IClientHandler;
import com.wisdom.mapp.core.interfaces.IHandler;
import com.wisdom.mapp.core.model.IMappDatapackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * 
 */
@Service
public class ClientHandler<T extends IMappDatapackage> implements IClientHandler<T> {

	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Autowired
	protected MyApplicationContext factory;

	protected void initContext(T requestObject) throws Exception {
		MappContext.initContext(null, requestObject, null);
	}
	
	/** 获取泛型 Class **/
	private Class<?> getPackageClass() throws Exception
	{
		ParameterizedType type = (ParameterizedType)getClass().getGenericInterfaces()[0];
		logger.debug(type.getActualTypeArguments()[0].toString());
		Class.forName(type.getActualTypeArguments()[0].toString()).newInstance();
		return (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
	}

	/**
	 * 初始化mapp上下文
	 * 
	 * @param request
	 *            ，requestObject
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	protected void initContext(T requestObject, Map<String, Object> attributes) throws Exception {
		try {
			/** 每次新生成一个上下文 **/
			MappContext.initContext(requestObject,attributes);
			
//			T request = (T) MappContext.getRequest();
//			logger.debug(request.toString());
			
//			if (attributes != null && attributes.isEmpty() == false)
//				MappContext.(attributes);
			/** 请求报文和返回报文报体结构一致，生成返回报文，并且设置包头与request一致 mler 2013-05-10 **/
//			T responseObject = (T) getPackageClass().newInstance();
			T responseObject = (T) requestObject.getClass().newInstance();
			responseObject.setHeader(requestObject.getHeader());
			MappContext.setResponse(responseObject);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new MappException(MappExceptionCode.UNEXPECT_ERROR, e);
		}
	}

	/**
	 * 传入请求对象，并调用方法获得结果
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws MappException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T doHandlePackage(T requestObject, Map<String, Object> attributes)
			throws Exception {

		initContext(requestObject, attributes);

		doHandle();

		return (T) MappContext.getResponse();
	}

	@Override
	/**
	 * 根据上下文中的request对象，调用接口，获取response对象并保存在上下文中
	 * 
	 * @param context
	 * @return
	 * @throws MappException
	 */
	public void doHandle() throws Exception 
	{
		IHandler actionHandler = findAction();
		actionHandler.doHandle();
	}

	/** 获取与bizcode对应的Action处理类 **/
	private IHandler findAction() {
		IHandler action = (IHandler) factory.getBean(MappContext.getRequest().getHeader().getBizCode());
		return action;
	}

	public void setFactory(MyApplicationContext factory) {
		this.factory = factory;
	}

}
