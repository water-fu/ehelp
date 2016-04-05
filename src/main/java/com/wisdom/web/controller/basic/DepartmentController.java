package com.wisdom.web.controller.basic;

import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Department;
import com.wisdom.service.service.basic.IDepartmentService;
import com.wisdom.common.entity.PageInfo;
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
 * 医院
 * Created by fusj on 16/3/2.
 */
@Controller
@RequestMapping("department")
public class DepartmentController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    private static final String VM_ROOT_PATH = "/help/basic/department/%s";

    @Autowired
    private IDepartmentService departmentService;

    /**
     * 医院信息首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 医院列表信息
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, Department department) {

        if(StringUtil.isNotEmptyObject(department.getName())) {
        	department.setName("%" + department.getName() + "%");
        }

        if(StringUtil.isNotEmptyObject(department.getSimplePinyin())) {
        	department.setSimplePinyin("%" + department.getSimplePinyin().toUpperCase() + "%");
        }

        pageInfo = departmentService.list(department, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);


        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增科室页面
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return String.format(VM_ROOT_PATH, "add");
    }

    /**
     * 新增保存
     * @param hospital
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(Department department) {
        try {

            int id = departmentService.addDepartment(department);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(id);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改页面
     * @param hospital
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView edit(Model model, Department department) {

    	department = departmentService.get(department.getId());

        model.addAttribute("department", department);

        return new ModelAndView(String.format(VM_ROOT_PATH, "edit"));
    }

    /**
     * 修改保存
     * @param hospital
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(Department department) {
        try {

        	departmentService.editDepartment(department);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param hospital
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(Department department) {
        try {

        	departmentService.deleteDepartment(department.getId());

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
