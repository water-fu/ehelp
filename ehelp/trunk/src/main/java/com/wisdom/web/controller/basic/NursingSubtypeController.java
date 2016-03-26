package com.wisdom.web.controller.basic;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.NursingSubtype;
import com.wisdom.service.service.basic.INursingSubtypeService;
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
 * 护理子项
 * Created by fusj on 16/3/10.
 */
@Controller
@RequestMapping("nursingSubtype")
public class NursingSubtypeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NursingSubtypeController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "basic/nursingSubtype/%s");

    @Autowired
    private INursingSubtypeService nursingSubtypeService;

    /**
     * 子项首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 子项列表数据
     * @param model
     * @param pageInfo
     * @param nursingSubtype
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView list(Model model, PageInfo pageInfo, NursingSubtype nursingSubtype) {

        pageInfo = nursingSubtypeService.list(nursingSubtype, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 根据主键获取对象
     * @param nursingSubtype
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(NursingSubtype nursingSubtype) {
        try {

            nursingSubtype = nursingSubtypeService.get(nursingSubtype.getId());

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(nursingSubtype);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 新增保存
     * @param nursingSubtype
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(NursingSubtype nursingSubtype) {
        try {

            nursingSubtypeService.add(nursingSubtype);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param nursingSubtype
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean modify(NursingSubtype nursingSubtype) {
        try {

            nursingSubtypeService.modify(nursingSubtype);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param nursingSubtype
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(NursingSubtype nursingSubtype) {
        try {

            nursingSubtypeService.delete(nursingSubtype);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
