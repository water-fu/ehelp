package com.wisdom.web.controller.common;

import com.wisdom.common.annotation.Check;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.StringUtil;
import com.wisdom.service.service.sys.IIdentifyCodeService;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证码
 * Created by fusj on 16/3/16.
 */
@Controller
@RequestMapping("identifyCode")
public class IdentifyCodeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyCodeController.class);

    @Autowired
    private IIdentifyCodeService identifyCodeService;

    /**
     * 发送验证码
     * @param phoneNo
     * @return
     */
    @RequestMapping(value = "sendCode", method = RequestMethod.POST)
    @ResponseBody
    @Check(loginCheck = false)
    public ResultBean sendIdentifyCode(String phoneNo, String type, HttpServletRequest request) {
        try {
            if(!StringUtil.isNotEmptyObject(phoneNo)) {
                throw new ApplicationException("请填写手机号码");
            }

            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }

            String code = identifyCodeService.sendIdentifyCode(phoneNo, type, ip);
            // 发送短信 phoneNo+code

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 校验码验证
     * @return
     */
    @RequestMapping(value = "validIdentifyCode", method = RequestMethod.GET)
    @ResponseBody
    @Check(loginCheck = false)
    public List validIdentifyCode(String fieldId, String fieldValue, String phoneNo) {
        List result = new ArrayList();
        result.add(fieldId);

        try {

            // 校验验证码是否正确
            Boolean flag = identifyCodeService.validIdentifyCode(phoneNo, fieldValue, SysParamDetailConstant.IDENTIFY_TYPE_REGISTER);
            if(!flag) {
                throw new ApplicationException("验证码错误");
            }

            result.add(true);

        } catch (Exception ex) {
            result.add(false);
        }

        return result;
    }
}
