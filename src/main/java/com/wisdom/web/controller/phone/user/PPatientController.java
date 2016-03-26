package com.wisdom.web.controller.phone.user;

import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 患者业务处理
 * Created by fusj on 16/3/20.
 */
@Controller
@RequestMapping("/p/patient")
public class PPatientController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PPatientController.class);

    private static final String VM_ROOT_PATH = String.format(PHONE_VM_ROOT, "patient/%s");

    /**
     * 注册完成选择页面
     * @return
     */
    @RequestMapping(value = "choose", method = RequestMethod.GET)
    public String choose() {
        return String.format(VM_ROOT_PATH, "choose");
    }

    /**
     * 认证页面
     * @return
     */
    @RequestMapping(value = "authen", method = RequestMethod.GET)
    public String authen() {
        return String.format(VM_ROOT_PATH, "authen");
    }
}
