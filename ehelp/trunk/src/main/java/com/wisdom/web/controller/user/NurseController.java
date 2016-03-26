package com.wisdom.web.controller.user;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Nurse;
import com.wisdom.service.service.user.INurseService;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 护士护工详细信息
 * Created by fusj on 16/3/14.
 */
@Controller
@RequestMapping("nurse")
public class NurseController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NurseController.class);

    @Autowired
    private INurseService nurseService;

    /**
     * 根据accountId获取护士信息
     * @param nurse
     * @return
     */
    @RequestMapping(value = "getByAccountId", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getByAccountId(Nurse nurse) {
        try {

            nurse = nurseService.getByAccountId(nurse);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(nurse);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
