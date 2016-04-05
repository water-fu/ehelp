package com.wisdom.web.controller.phone.user;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.dao.entity.Patient;
import com.wisdom.service.service.user.IPatientService;
import com.wisdom.weChat.service.IMediaService;
import com.wisdom.web.common.BaseController;
import com.wisdom.web.common.constants.CommonConstant;
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
 * 患者业务处理
 * Created by fusj on 16/3/20.
 */
@Controller
@RequestMapping("/p/patient")
public class PPatientController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PPatientController.class);

    private static final String VM_ROOT_PATH = String.format(PHONE_VM_ROOT, "patient/%s");

    @Autowired
    private IMediaService mediaDownload;

    @Autowired
    private IPatientService patientService;

    /**
     * 注册成功页面
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public String success() {
        return String.format(VM_ROOT_PATH, "success");
    }

    /**
     * 认证页面
     * @return
     */
    @RequestMapping(value = "identification", method = RequestMethod.GET)
    public String identification() {
        return String.format(VM_ROOT_PATH, "identification");
    }

    /**
     * 患者认证页面
     * @param patient
     * @param headImg
     * @param bodyImg
     * @param request
     * @return
     */
    @RequestMapping(value = "identification", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean identification(Patient patient, String headImg, String bodyImg, HttpServletRequest request) {
        try {

            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);

            byte[] head = mediaDownload.getMediaFile(headImg);
            byte[] body = mediaDownload.getMediaFile(bodyImg);

            patientService.identification(patient, head, body, cookie.getValue());

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 患者首页
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }
}
