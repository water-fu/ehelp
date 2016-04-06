package com.wisdom.web.controller.phone.system;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Account;
import com.wisdom.dao.entity.WeChatLogin;
import com.wisdom.service.service.sys.IWeChatLoginService;
import com.wisdom.service.service.user.IAccountService;
import com.wisdom.weChat.config.WeChatSetting;
import com.wisdom.web.common.BaseController;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.LoginUrlConstants;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 微信登陆
 * Created by fusj on 16/4/6.
 */
@Controller
@RequestMapping("weChatLogin")
public class WeChatLoginController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatLoginController.class);

    // 编码
    private static final String ENCODE = "utf-8";

    // 微信登陆state
    private static final String STATE = "1qaz2wsx";

    // 直接授权登陆
    private static final String SCOPE_SNSAPI_BASE = "snsapi_base";

    // 请求用户基本信息
    private static final String SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";

    @Autowired
    private WeChatSetting weChatSetting;

    @Autowired
    private IWeChatLoginService weChatLoginService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private SessionCache sessionCache;

    /**
     * 微信登陆
     * @param request
     * @param response
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuffer sb = new StringBuffer();
            // 微信回调路径
            sb.append("http://").append(request.getServerName())
                    .append(request.getContextPath())
                    .append("/weChatLogin").append("/loginCall");

            String url = LoginUrlConstants.WE_CHAT_LOGIN.replace("APPID", weChatSetting.getAppId())
                    .replace("REDIRECT_URI", URLEncoder.encode(sb.toString(), ENCODE))
                    .replace("SCOPE", SCOPE_SNSAPI_USERINFO)
                    .replace("STATE", STATE);

            logger.info("回调地址:" + sb.toString());
            logger.info("微信登陆地址:" + url);

            response.sendRedirect(url);

        } catch (Exception ex) {
            logger.error("微信登陆异常:" + ex.getMessage(), ex);
        }
    }

    /**
     * 用户授权后，登陆系统，如果取消登陆，则跳转到登陆页面
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "loginCall", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "code", required = false) String code, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("code:" + code);
            logger.info("state:" + state);

            if(!StringUtil.isNotEmptyObject(code)) {
                return new ModelAndView("redirect:/");
            }

            if(!StringUtil.isNotEmptyObject(state) || !STATE.equals(state)) {
                return new ModelAndView("redirect:/");
            }

            WeChatLogin weChatLogin = weChatLoginService.login(code, request);

            Account account = null;
            if(null != weChatLogin.getAccountId()) {
                account = accountService.get(weChatLogin.getAccountId());
            }

            try {
                // 保存session
                SessionDetail sessionDetail = new SessionDetail();
                sessionDetail.setFrom(SysParamDetailConstant.LOGIN_FROM_WECHAT);
                if(null != account) {
                    sessionDetail.setAccountId(account.getId());
                    sessionDetail.setType(account.getType());
                    sessionDetail.setPhoneNo(account.getPhoneNo());
                    sessionDetail.setName(account.getName());
                    sessionDetail.setStatus(account.getStatus());
                } else {
                    sessionDetail.setName(weChatLogin.getNickName());
                    sessionDetail.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_NEW);
                }

                sessionDetail.setWeChatLogin(weChatLogin);

                // 把redis的key存入cookie，有效期1天
                String value = UUID.randomUUID().toString();
                CookieUtil.addCookie(response, CommonConstant.COOKIE_VALUE, value, CommonConstant.SESSION_TIME_OUT_DAY);

                // 把用户登陆信息存入redis
                sessionCache.put(value, sessionDetail, CommonConstant.SESSION_TIME_OUT_DAY);

            } catch (Exception ex) {
                logger.error("缓存redis异常:" + ex.getMessage(), ex);
            }

            return new ModelAndView("redirect:/p/patient/success");

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ModelAndView("redirect:/");
        }
    }
}
