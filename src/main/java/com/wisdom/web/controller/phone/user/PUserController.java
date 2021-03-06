package com.wisdom.web.controller.phone.user;

import com.wisdom.common.annotation.Check;
import com.wisdom.common.cache.SessionCache;
import com.wisdom.mapp.core.annotation.Response;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.dao.entity.Account;
import com.wisdom.service.service.sys.IIdentifyCodeService;
import com.wisdom.service.service.user.IAccountService;
import com.wisdom.web.common.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 前端用户注册、登陆、认证类
 * Created by fusj on 16/3/17.
 */
@Controller
@RequestMapping("/p")
public class PUserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PUserController.class);

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private IIdentifyCodeService identifyCodeService;

    @Autowired
    private IAccountService accountService;

    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "{type}/register", method = RequestMethod.GET)
    @Check(loginCheck = false)
    public ModelAndView register(Model model, @PathVariable("type") String type) {
        model.addAttribute("type", type);

        return new ModelAndView(String.format(PHONE_VM_ROOT, "register"));
    }

    /**
     * 注册
     * @param account
     * @param code
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean register(Account account, String code, HttpServletRequest request, HttpServletResponse response) {
        try {

            // 校验验证码是否正确
            Boolean flag = identifyCodeService.validIdentifyCode(account.getPhoneNo(), code, SysParamDetailConstant.IDENTIFY_TYPE_REGISTER);
            if(!flag) {
                throw new ApplicationException("验证码错误");
            }

            // 保存account表
            account = accountService.register(account);

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
            resultBean.setData(transType(account.getType()));

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }


    /**
     * 校验手机号码
     * @return
     */
    @RequestMapping(value = "validPhoneNo", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public List validPhoneNo(String fieldId, String fieldValue) {
        List result = new ArrayList();
        result.add(fieldId);

        try {
            Account account = new Account();
            account.setPhoneNo(fieldValue);

            List<Account> list = accountService.list(account);
            if(CollectionUtils.isEmpty(list)) {
                result.add(true);
            } else {
                result.add(false);
            }
        } catch (Exception ex) {
            result.add(false);
        }

        return result;
    }

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = "{type}/login", method = RequestMethod.GET)
    @Check(loginCheck = false)
    public ModelAndView login(Model model, @PathVariable("type") String type) {

        model.addAttribute("type", type);

        return new ModelAndView(String.format(PHONE_VM_ROOT, "login"));
    }

    /**
     * 登陆
     * @param account
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean login(Account account, HttpServletRequest request, HttpServletResponse response) {
        try {

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
            resultBean.setData(transType(account.getType()));

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 忘记密码页面
     * @return
     */
    @RequestMapping(value = "forget", method = RequestMethod.GET)
    @Check(loginCheck = false)
    public String forget() {
        return String.format(PHONE_VM_ROOT, "forget");
    }

    /**
     * 忘记密码
     * @param account
     * @return
     */
    @RequestMapping(value = "forget", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean forget(Account account, String code) {
        try {
            // 校验验证码是否正确
            Boolean flag = identifyCodeService.validIdentifyCode(account.getPhoneNo(), code, SysParamDetailConstant.IDENTIFY_TYPE_FOTGET);
            if(!flag) {
                throw new ApplicationException("验证码错误");
            }

            // 修改account表
            account = accountService.forget(account);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(transType(account.getType()));

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 账号绑定
     * @return
     */
    @RequestMapping(value = "{type}/bind", method = RequestMethod.GET)
    public ModelAndView bind(Model model, @PathVariable("type") String type) {
        model.addAttribute("type", type);

        return new ModelAndView(String.format(PHONE_VM_ROOT, "bind"));
    }

    /**
     * 账号绑定
     * @return
     */
    @RequestMapping(value = "bind", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean bind(Account account, String code, HttpServletRequest request, HttpServletResponse response) {
        try {

            // 校验验证码是否正确
            Boolean flag = identifyCodeService.validIdentifyCode(account.getPhoneNo(), code, SysParamDetailConstant.IDENTIFY_TYPE_BIND);
            if(!flag) {
                throw new ApplicationException("验证码错误");
            }

            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            account = accountService.bind(account, sessionDetail);

            // 更新session内容
            sessionDetail.setAccountId(account.getId());
            sessionDetail.setPhoneNo(account.getPhoneNo());
            sessionDetail.setStatus(account.getStatus());

            sessionCache.put(cookie.getValue(), sessionDetail, CommonConstant.SESSION_TIME_OUT_DAY);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(transType(account.getType()));

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
