package com.wisdom.mapp.action;

import com.wisdom.mapp.core.JsonMapper;
import com.wisdom.mapp.core.base.BaseActionHandler;
import com.wisdom.mapp.core.model.IBody;
import com.wisdom.mapp.util.BeanValidators;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Map;

/**
 * action的抽象类
 * Created by fusj on 16/1/15.
 */
public abstract class AbstractBaseActionHandler<Request extends IBody, Response extends IBody>
		extends BaseActionHandler<Request, Response> {
	@Autowired
	protected Validator validator;
	
	protected static JsonMapper binder = JsonMapper.nonDefaultMapper();

	/**
	 * 注解验证：如为空，长度等
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected void doCheckModel() throws Exception {
		try {
			BeanValidators.validateWithException((Validator) validator,
					this.request);
		} catch (ConstraintViolationException e) {
			Map mapResult = BeanValidators.extractPropertyAndMessage(e);
			throw new Exception(binder.toJson(mapResult));
		}
	}
}
