package com.wisdom.web.controller.system;

import com.wisdom.common.annotation.Check;
import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.dao.entity.Account;
import com.wisdom.service.service.user.IAccountService;
import com.wisdom.web.common.BaseController;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 后台用户登陆、退出登陆等
 * Created by fusj on 16/3/10.
 */
@Controller
@RequestMapping("")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private IAccountService accountService;

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = {"", "/", "login"}, method = RequestMethod.GET)
    @Check(loginCheck = false)
    public String index() {
        return String.format(MANAGER_VM_ROOT, "login");
    }

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = {"toLogin"}, method = RequestMethod.GET)
    @Check(loginCheck = false)
    public String toLogin() {
        return String.format(MANAGER_VM_ROOT, "login");
    }

    /**
     * 登陆
     * @param userName
     * @param password
     * @param response
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean login(String userName, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            Account account = new Account();
            account.setPhoneNo(userName);
            account.setPassword(password);
            account.setType(SysParamDetailConstant.ACCOUNT_TYPE_PATIENT);

            account = accountService.login(account);

            // session缓存失败，不影响用户注册
            try {
                // session到redis的对象
                SessionDetail sessionDetail = new SessionDetail();
                sessionDetail.setAccountId(account.getId());
                sessionDetail.setPhoneNo(account.getPhoneNo());
                sessionDetail.setType(account.getType());
                sessionDetail.setStatus(account.getStatus());
                sessionDetail.setFrom(SysParamDetailConstant.LOGIN_FROM_SYSTEM);

                // 把redis的key存入cookie，有效期1天
                Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
                String value;
                if(cookie != null) {
                    value = cookie.getValue();
                } else {
                    value = UUID.randomUUID().toString();
                }
                CookieUtil.addCookie(response, CommonConstant.COOKIE_VALUE, value, CommonConstant.SESSION_TIME_OUT_DAY);

                // 把用户登陆信息存入redis
                sessionCache.put(value, sessionDetail, CommonConstant.SESSION_TIME_OUT_DAY);

            } catch (Exception ex) {
                logger.error("缓存redis异常:" + ex.getMessage(), ex);
            }

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 退出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean logout(HttpServletRequest request, HttpServletResponse response) {
        try {

            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            if(null != cookie) {
                String uid = cookie.getValue();
                cookie.setMaxAge(0);

                sessionCache.evict(uid);
            }

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
