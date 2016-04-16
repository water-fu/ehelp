package com.wisdom.web.controller.phone.user;

import com.wisdom.common.annotation.Check;
import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.dao.entity.Recover;
import com.wisdom.service.service.user.IRecoverService;
import com.wisdom.weChat.service.IMediaService;
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

/**
 * 康复师
 * Created by fusj on 16/4/9.
 */
@Controller
@RequestMapping("/p/recover")
public class PRecoverController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PRecoverController.class);

    private static final String VM_ROOT_PATH = String.format(PHONE_VM_ROOT, "recover/%s");

    @Autowired
    private IMediaService mediaDownload;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private IRecoverService recoverService;

    /**
     * 注册成功页面
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public String success() {
        return String.format(VM_ROOT_PATH, "success");
    }

    /**
     * 康复师首页
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 康复师个人简况
     * @return
     */
    @RequestMapping(value = "personalInfo", method = RequestMethod.GET)
    public String personalInfo() {
        return String.format(VM_ROOT_PATH, "personalInfo");
    }

    /**
     * 康复师个人简介保存
     * @return
     */
    @RequestMapping(value = "personalInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean personalInfo(Recover recover, HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            recoverService.personalInfo(recover, sessionDetail);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 认证页面(微信浏览器)
     * @return
     */
    @RequestMapping(value = "identification", method = RequestMethod.GET)
    @Check(phoneCheck = true)
    public String identification() {
        return String.format(VM_ROOT_PATH, "identification");
    }

    /**
     * 认证页面(非微信浏览器)
     * @return
     */
    @RequestMapping(value = "identification_o", method = RequestMethod.GET)
    @Check(phoneCheck = true)
    public String identification_o() {
        return String.format(VM_ROOT_PATH, "identification_o");
    }

    /**
     * 患者认证(微信浏览器)
     * @param recover
     * @param headImg
     * @param bodyImg
     * @param request
     * @return
     */
    @RequestMapping(value = "identification", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean identification(Recover recover, String headImg, String bodyImg, HttpServletRequest request) {
        try {

            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            // 从微信后台获取图片
            byte[] head = mediaDownload.getMediaFile(headImg);
            byte[] body = mediaDownload.getMediaFile(bodyImg);

            recoverService.identification(recover, head, body, sessionDetail);

            // 更新session的账户状态
            sessionDetail.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_AUTHEN);
            sessionCache.put(cookie.getValue(), sessionDetail, CommonConstant.SESSION_TIME_OUT_DAY);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 患者认证(非微信浏览器)
     * @param recover
     * @param headImg
     * @param bodyImg
     * @param request
     * @return
     */
    @RequestMapping(value = "identification_o", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean identification_o(Recover recover, String headImg, String bodyImg, HttpServletRequest request) {
        try {

            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            recoverService.identification(recover, headImg, bodyImg, sessionDetail);

            // 更新session的账户状态
            sessionDetail.setStatus(SysParamDetailConstant.ACCOUNT_STATUS_AUTHEN);
            sessionCache.put(cookie.getValue(), sessionDetail, CommonConstant.SESSION_TIME_OUT_DAY);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 证件上传
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String upload() {
        return String.format(VM_ROOT_PATH, "upload");
    }
}
