package com.wisdom.common.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.ExceptionCodeConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.ResultExceptionCode;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.common.util.JackonUtil;
import com.wisdom.common.util.SpringBeanUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 登陆拦截器
 * Created by fusj on 16/3/10.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        List<String> notNeedValid = new ArrayList<>();
        // 后台用户
        notNeedValid.add("");
        notNeedValid.add("/");
        notNeedValid.add("/login");
        notNeedValid.add("/login/");

        // 前段用户
        notNeedValid.add("/p/register");
        notNeedValid.add("/p/register/");

        notNeedValid.add("/p/validPhoneNo");
        notNeedValid.add("/p/validPhoneNo/");

        notNeedValid.add("/p/sendCode");
        notNeedValid.add("/p/sendCode/");

        String url = request.getRequestURI();
        if(url.startsWith("/static")) {
            return true;
        }

        for(String str : notNeedValid) {
            if(str.equals(url)) {
                return true;
            }
        }

        SessionCache sessionCache = (SessionCache) SpringBeanUtil.getSpringBean("sessionCache");

        Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
        // 浏览器cookie过期
        if(null == cookie) {
//            return loginTimeOut(response);
        } else {
            String key = cookie.getValue();

            // redis的session过期
            Object obj = sessionCache.get(key);
            if(obj == null) {

                // return loginTimeOut(response);
            } else {
                // 重新设置redis的失效时间
                sessionCache.put(key, obj, CommonConstant.SESSION_TIME_OUT);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 登陆超时
     * @param response
     * @return
     * @throws Exception
     */
    private boolean loginTimeOut(HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        // 登陆超时CODE
        ResultExceptionCode code = new ResultExceptionCode();
        code.setCode(ExceptionCodeConstant.LOGIN_TIME_OUT);

        // 登陆超时返回bean
        ResultBean resultBean = new ResultBean(false);
        resultBean.setData(code);

        response.getWriter().write(JackonUtil.writeEntity2JSON(resultBean));

        return false;
    }
}
