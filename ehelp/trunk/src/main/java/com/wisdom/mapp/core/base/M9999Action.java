package com.wisdom.mapp.core.base;

import com.wisdom.mapp.core.ErrorCodeDefine;
import com.wisdom.mapp.core.MyApplicationContext;
import com.wisdom.mapp.core.annotation.Action;
import com.wisdom.mapp.core.exception.BusinessException;
import com.wisdom.mapp.core.exception.DefaultExceptionCode;
import com.wisdom.mapp.core.exception.SystemException;
import com.wisdom.mapp.core.model.IBody;
import com.wisdom.mapp.core.model.M9999Request;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("m9999")
@Action(bizcode="m9999",userCheck=false,ipCheck=false)
public class M9999Action extends BaseActionHandler<M9999Request, IBody> {

	@Autowired
	protected MyApplicationContext factory;
	
	private final String defaultKey = "mappClient";
	
	@Override
	protected void doAction() throws BusinessException, SystemException, Exception {
		
		if(StringUtils.isBlank(this.request.getMethod()))
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"查找调用Mehtod为空 "));
		
		Object service = null;
		
		if(StringUtils.isBlank(this.request.getInterfaceKey()) == false)
		{
//			throw new BusinessException(ErrorCodeDefine.EXPECT_ERROR,"查找服务对象的key为空 ");
			service = factory.getBean(this.request.getInterfaceKey());
		}
		
		if(service == null && StringUtils.isBlank(this.request.getInterfaceClass()) == false)
		{
//			throw new BusinessException(ErrorCodeDefine.EXPECT_ERROR,"查找服务对象的key为空 ");
			service = factory.getBean(Class.forName(this.request.getInterfaceClass()));
		}
		
		if(service == null)
		{
			service = factory.getBean(defaultKey);
		}
		
		if(service == null)
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"服务对象(key="+this.request.getInterfaceKey()+")为空 "));
		
		Object result = null;
		
		if(this.request.getParams() == null || this.request.getParams().isEmpty())
			result = MethodUtils.invokeExactMethod(service, this.request.getMethod(), null);
		else
			result = MethodUtils.invokeExactMethod(service, this.request.getMethod(),this.request.getParams().toArray());
		
		if(result != null && result instanceof IBody == false)
		{
			throw new BusinessException(new DefaultExceptionCode(ErrorCodeDefine.EXPECT_ERROR,"返回结果必须为IBody实现"));
		}
		
		this.response = (IBody)result;
	}

}
