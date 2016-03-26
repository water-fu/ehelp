package com.wisdom.web.controller.user;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Account;
import com.wisdom.service.service.user.IAccountService;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 护士管理
 * Created by fusj on 16/3/14.
 */
@Controller
@RequestMapping("nursing")
public class AccountNursingController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountNursingController.class);

    // 护士vm
    private static final String N_VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "user/account/nursing/%s");

    @Autowired
    private IAccountService accountService;

    /**
     * 护士首页
     * @return
     */
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public String nursing() {
        return String.format(N_VM_ROOT_PATH, "index");
    }

    /**
     * 护士列表数据
     * @param model
     * @param pageInfo
     * @param account
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView nursingList(Model model, PageInfo pageInfo, Account account) {
        account.setType(SysParamDetailConstant.ACCOUNT_TYPE_NURSING);

        pageInfo = accountService.list(account, pageInfo);
        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(N_VM_ROOT_PATH, "list"));
    }

    /**
     * 账号新增
     * @param account
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean nursingAdd(Account account) {
        try {

            // 新增患者
            account.setType(SysParamDetailConstant.ACCOUNT_TYPE_NURSING);

            accountService.add(account);

            ResultBean resultBean = new ResultBean(true);
            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 保存修改
     * @param account
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean nursingModify(Account account) {
        try {

            accountService.modify(account);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
