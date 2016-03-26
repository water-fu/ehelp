package com.wisdom.web.controller.system;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index() {
        return String.format(MANAGER_VM_ROOT, "login");
    }

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = {"toLogin"}, method = RequestMethod.POST)
    public String toLogin() {
        return String.format(MANAGER_VM_ROOT, "login");
    }

    /**
     * 登陆
     * @param userName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean login(String userName, String password, HttpServletRequest request) {
        try {
            sessionCache.put(UUID.randomUUID().toString(), userName, 30 * 60);

            ResultBean resultBean = new ResultBean(true);


            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }


}
