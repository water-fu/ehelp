package com.wisdom.weChat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验Token是否过期
 * Created by fusj on 15/12/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Token {
    /**
     * 是否需要检验Token
     * @return
     */
    boolean tokenCheck() default true;
}
