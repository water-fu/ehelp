package com.wisdom.web.controller.system;

import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.SysParam;
import com.wisdom.service.service.sys.ISysParamService;
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
 * 缓存数据
 * Created by fusj on 16/3/9.
 */
@Controller
@RequestMapping("param")
public class SysParamController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SysParamController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "system/param/%s");

    @Autowired
    private ISysParamService sysParamService;

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 列表数据
     * @param model
     * @param pageInfo
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, SysParam sysParam) {

        pageInfo = sysParamService.list(sysParam, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(SysParam sysParam) {
        try {

            sysParamService.add(sysParam);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 获取详细信息
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean modify(SysParam sysParam) {
        try {

            sysParam = sysParamService.get(sysParam.getId());

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(sysParam);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean modifySave(SysParam sysParam) {
        try {

            sysParamService.modify(sysParam);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 详细页面
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail() {
        return String.format(VM_ROOT_PATH, "detail");
    }

    /**
     * 详细列表
     * @param model
     * @param pageInfo
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "detailList", method = RequestMethod.POST)
    public ModelAndView detailList(Model model, PageInfo pageInfo, SysParam sysParam) {

        pageInfo = sysParamService.detailList(sysParam, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "detailList"));
    }

    /**
     * 新增
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "detailAdd", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean detailAdd(SysParam sysParam) {
        try {

            sysParamService.detailAdd(sysParam);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "detailModify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean detailModify(SysParam sysParam) {
        try {

            sysParamService.detailModify(sysParam);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除子项
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(SysParam sysParam) {
        try {

            sysParamService.delete(sysParam.getId());

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
