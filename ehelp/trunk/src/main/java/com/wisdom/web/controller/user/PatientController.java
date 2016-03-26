package com.wisdom.web.controller.user;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Patient;
import com.wisdom.service.service.user.IPatientService;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 患者实名认证信息
 * Created by fusj on 16/3/14.
 */
@Controller
@RequestMapping("patient")
public class PatientController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private IPatientService patientService;

    /**
     * 根据accountId获取患者实名信息
     * @param patient
     * @return
     */
    @RequestMapping(value = "getAuditByAccountId", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAuditByAccountId(Patient patient) {
        try {

            patient = patientService.getAuditByAccountId(patient);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(patient);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
