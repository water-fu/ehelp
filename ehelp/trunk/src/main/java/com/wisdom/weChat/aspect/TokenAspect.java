package com.wisdom.weChat.aspect;

import com.wisdom.weChat.annotation.Token;
import com.wisdom.weChat.config.AccessTokenSetting;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 校验Token切面
 * Created by fusj on 15/12/23.
 */
@Aspect
@Component
public class TokenAspect {

    private static Logger logger = LoggerFactory.getLogger(TokenAspect.class);

    @Autowired
    private AccessTokenSetting accessTokenSetting;

    private String synchronize = "synchronize";

    /**
     * 解析是否需要判断Token令牌过期校验
     *
     * @param
     * @throws
     */
    @Before("execution(public * com.wisdom.weChat.service.impl..*Impl.*(..))")
    public void before(JoinPoint point) throws Throwable {

        Method targetMethod = null;

        Method[] methods = point.getTarget().getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(point.getSignature().getName().equals(method.getName())) {
                targetMethod = method;
                break;
            }
        }

        if(null == targetMethod) {
            return;
        }

        Token token = targetMethod.getAnnotation(Token.class);

        if(null != token && token.tokenCheck()) {
            // 刷新access_token是同步的
            synchronized (synchronize) {
                long currentTime = new Date().getTime();
                long loadTime = accessTokenSetting.getLoadTime();

                // 过期前半小时加载
                if((currentTime - loadTime) / 1000 > accessTokenSetting.getExpiresIn() - 15 * 60) {
                    logger.info("Reload TokenAccess");
                    accessTokenSetting.initAccessToken();
                }
            }
        }
    }
}