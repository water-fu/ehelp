package com.wisdom.mapp.core.aspect;

import com.wisdom.mapp.core.annotation.Crypt;
import com.wisdom.mapp.core.configuration.MappSettings;
import com.wisdom.mapp.core.util.DESedeUtils;
import com.wisdom.mapp.core.util.GZipUtils;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class CryptAspect {
	
	Logger logger = LoggerFactory.getLogger(CryptAspect.class);
	
//	@Value("${encryptPermission}") 
//	private boolean cryptPermission;
	
	@Autowired
	private MappSettings mappSettings;
	
	/**
	 * 拦截处理actionHandle方法，在此方法前执行已经定义的filter
	 * @param context
	 * @throws Throwable 
	 */
	@Around("within(com.wisdom.mapp..*) && @annotation(crypt)")
	public Object AroundCrypt(ProceedingJoinPoint point,Crypt crypt) throws Throwable
	{
		logger.debug("进入拦截器 AroundCrypt.AroundCrypt。");
		
		MethodSignature joinPointObject = (MethodSignature) point.getSignature();
        Method method = joinPointObject.getMethod();    
        Crypt crypt_annotation = method.getAnnotation(Crypt.class);
		
		Object[] args = point.getArgs();
		
		if(mappSettings.isEncryptPermission() == false || crypt_annotation == null)
			return point.proceed(args);
		
		logger.debug("注释 @Crypt( crypt_annotation="+crypt_annotation.algorithm()+",desKey="+crypt_annotation.desKey()+",base64Wrapped="+crypt_annotation.base64Wrapped()+" )");
		/**
		 * 当拦截的方法第一个参数对象为String类型时，将参数进行解密操作
		 */
		if(args != null && args.length != 0 && args[0].getClass() == String.class)
		{
			logger.debug("对参数进行解密操作。");
			
			byte[] arg0_byte = ((String)args[0]).getBytes("utf-8");
			
			if(crypt_annotation.base64Wrapped())
				arg0_byte = Base64.decodeBase64((String)args[0]);
			
			if(crypt_annotation.gzip())
				arg0_byte = GZipUtils.decompress(arg0_byte);
			
			args[0] = DESedeUtils.decrypt(arg0_byte, crypt_annotation.desKey().getBytes("utf-8"));
		
		}
		
		Object result = point.proceed(args);
		
		/**
		 * 如果返回参数为String 则加密返回参数
		 */
		if(method.getReturnType() == String.class)
		{
			result = DESedeUtils.encrypt(((String)result).getBytes("utf-8"), crypt_annotation.desKey().getBytes("utf-8"));
			
			byte[] ret_byte = ((String)result).getBytes("utf-8");
			
			if(crypt_annotation.gzip())
				ret_byte = GZipUtils.compress(ret_byte);
			
			if(crypt_annotation.base64Wrapped())
				ret_byte = Base64.decodeBase64(ret_byte);
			
			return new String(ret_byte,"utf-8");
		}
		
		return result;
	}
	
}
