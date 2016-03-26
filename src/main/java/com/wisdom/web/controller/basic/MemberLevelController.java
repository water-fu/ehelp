package com.wisdom.web.controller.basic;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Department;
import com.wisdom.dao.entity.Hospital;
import com.wisdom.dao.entity.MemberLevel;
import com.wisdom.dao.mapper.HospitalMapper;
import com.wisdom.service.service.basic.IDepartmentService;
import com.wisdom.service.service.basic.IHospitalService;
import com.wisdom.service.service.basic.IMemberLevelService;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.web.common.BaseController;

import org.apache.commons.lang.StringUtils;
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
 * 积分管理
 * @author wb-huweiying
 *
 */
@Controller
@RequestMapping("memberlevel")
public class MemberLevelController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MemberLevelController.class);

    private static final String VM_ROOT_PATH = "/help/basic/memberlevel/%s";

    @Autowired
    private IMemberLevelService memberLevelService;

    /**
     * 积分等级信息首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 积分等级列表信息
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, MemberLevel memberLevel) {

        if(StringUtil.isNotEmptyObject(memberLevel.getName())) {
        	memberLevel.setName("%" + memberLevel.getName() + "%");
        }

        pageInfo = memberLevelService.list(memberLevel, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);


        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增跳转
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return String.format(VM_ROOT_PATH, "add");
    }

    /**
     * 新增积分等级
     * @param memberLevel
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(MemberLevel memberLevel) {
        try {
            int id = memberLevelService.add(memberLevel);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(id);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改页面
     * @param memberLevel
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView edit(Model model, MemberLevel memberLevel) {

    	memberLevel = memberLevelService.get(memberLevel.getId());

        model.addAttribute("memberlevel", memberLevel);

        return new ModelAndView(String.format(VM_ROOT_PATH, "edit"));
    }

    /**
     * 修改保存
     * @param memberLevel
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(MemberLevel memberLevel) {
        try {

        	memberLevelService.editMemberLevel(memberLevel);
            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param memberLevel
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(MemberLevel memberLevel) {
        try {
        	
        	memberLevelService.deleteMemberLevel(memberLevel.getId());
            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
