package com.wisdom.web.controller.basic;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.NursingType;
import com.wisdom.service.service.basic.INursingSubtypeService;
import com.wisdom.service.service.basic.INursingTypeService;
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
 * 护理类型管理
 * Created by fusj on 16/3/6.
 */
@Controller
@RequestMapping("nursingType")
public class NursingTypeController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(NursingTypeController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "basic/nursingType/%s");

    @Autowired
    private INursingTypeService nursingTypeService;

    @Autowired
    private INursingSubtypeService nursingSubtypeService;

    /**
     * 护理类型管理首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 护理大类列表页
     * @param model
     * @param nursingType
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, NursingType nursingType) {

        pageInfo = nursingTypeService.list(nursingType, pageInfo);

        model.addAttribute("pageInfo", pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增保存
     * @param nursingType
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(NursingType nursingType) {
        try {
            nursingTypeService.add(nursingType);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改页面
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(NursingType nursingType) {
        try {
            nursingType = nursingTypeService.get(nursingType.getId());

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(nursingType);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param nursingType
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean modify(NursingType nursingType) {
        try {
            nursingTypeService.modify(nursingType);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param nursingType
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(NursingType nursingType) {
        try {
            nursingTypeService.delete(nursingType.getId());

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
