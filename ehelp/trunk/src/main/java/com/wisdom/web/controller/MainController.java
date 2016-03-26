package com.wisdom.web.controller;

import com.wisdom.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 系统
 * Created by fusj on 16/3/16.
 */
@Controller
@RequestMapping
public class MainController extends BaseController {

    /**
     * 后台管理首页
     * @return
     */
    @RequestMapping(value = {"/manager", "/manager/"}, method = RequestMethod.GET)
    public String index() {

        return String.format(MANAGER_VM_ROOT, "index");
    }
}
