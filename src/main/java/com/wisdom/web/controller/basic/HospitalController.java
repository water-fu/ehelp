package com.wisdom.web.controller.basic;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Hospital;
import com.wisdom.dao.mapper.HospitalMapper;
import com.wisdom.service.service.basic.IHospitalService;
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
 * 医院
 * Created by fusj on 16/3/2.
 */
@Controller
@RequestMapping("hospital")
public class HospitalController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "basic/hospital/%s");

    @Autowired
    private IHospitalService hospitalService;

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
    public ModelAndView list(Model model, PageInfo pageInfo, Hospital hospital) {

        if(StringUtil.isNotEmptyObject(hospital.getName())) {
            hospital.setName("%" + hospital.getName() + "%");
        }

        if(StringUtil.isNotEmptyObject(hospital.getSimplePinyin())) {
            hospital.setSimplePinyin("%" + hospital.getSimplePinyin().toUpperCase() + "%");
        }

        pageInfo = hospitalService.list(hospital, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);


        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增医院页面
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
    public ResultBean add(Hospital hospital) {
        try {

            int id = hospitalService.addHospital(hospital);

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
    public ModelAndView edit(Model model, Hospital hospital) {

        hospital = hospitalService.get(hospital.getId());

        model.addAttribute("hospital", hospital);

        return new ModelAndView(String.format(VM_ROOT_PATH, "edit"));
    }

    /**
     * 修改保存
     * @param hospital
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(Hospital hospital) {
        try {

            hospitalService.editHospital(hospital);

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
    public ResultBean delete(Hospital hospital) {
        try {

            hospitalService.deleteHospital(hospital.getId());

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
