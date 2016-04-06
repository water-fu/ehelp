package com.wisdom.web.common.interceptor;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.ExceptionCodeConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.ResultExceptionCode;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.common.util.JackonUtil;
import com.wisdom.common.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        List<String> notNeedValid = new ArrayList<>();
        // 后台用户
        notNeedValid.add("/login");

        // 前段用户
        notNeedValid.add("/p/login");
        notNeedValid.add("/p/register");
        notNeedValid.add("/p/forget");
        notNeedValid.add("/identifyCode/validPhoneNo");
        notNeedValid.add("/identifyCode/sendCode");

        notNeedValid.add("/p/patient/success");

        // 微信的接口
        notNeedValid.add("/weChat");

        String url = request.getRequestURI();

        // 静态资源
        String staticPath = request.getContextPath() + "/static";
        if(url.startsWith(staticPath)) {
            return true;
        }

        // 访问地址
        for(String str : notNeedValid) {
            if(url.equals(request.getContextPath()) || url.equals(request.getContextPath() + "/")) {
                return true;
            }

            str = request.getContextPath() + str;
            if(url.startsWith(str)) {
                return true;
            }
        }

        SessionCache sessionCache = (SessionCache) SpringBeanUtil.getSpringBean("sessionCache");

        Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
        // 浏览器cookie过期
        if(null == cookie) {
            logger.error("cookie为空");
            return loginTimeOut(request, response, url);
        } else {
            String key = cookie.getValue();
//            logger.error("cookie:" + key);
            // redis的session过期
            Object obj = sessionCache.get(key);
            if(obj == null) {
                return loginTimeOut(request, response, url);
            } else {
                // 重新设置redis的失效时间
                sessionCache.put(key, obj, CommonConstant.SESSION_TIME_OUT_DAY);
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
    private boolean loginTimeOut(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")){

            response.setContentType("application/json");

            // 登陆超时CODE
            ResultExceptionCode code = new ResultExceptionCode();
            code.setCode(ExceptionCodeConstant.LOGIN_TIME_OUT);

            // 登陆超时返回bean
            ResultBean resultBean = new ResultBean(false);
            resultBean.setData(code);

            response.getWriter().print(JackonUtil.writeEntity2JSON(resultBean));
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("http://").append(request.getServerName())
                    .append(":").append(request.getServerPort())
                    .append(request.getContextPath());
            if(url.contains("/p/")) {
                sb.append("/p/login");
            } else {
                sb.append("/login");
            }


            response.sendRedirect(sb.toString());
        }

        return false;
    }
}
