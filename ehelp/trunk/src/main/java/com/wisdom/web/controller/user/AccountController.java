package com.wisdom.web.controller.user;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Account;
import com.wisdom.service.service.user.IAccountService;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 账户管理
 * Created by fusj on 16/3/14.
 */
@Controller
@RequestMapping("account")
public class AccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private IAccountService accountService;

    /**
     * 根据主键获取
     * @param account
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Account account) {
        try {

            account = accountService.get(account);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(account);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param account
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(Account account) {
        try {

            accountService.delete(account);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
