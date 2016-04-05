package com.wisdom.web.controller.basic;

import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Device;
import com.wisdom.service.service.basic.IDeviceService;
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
 * 医疗器械
 * Created by fusj on 16/3/13.
 */
@Controller
@RequestMapping("device")
public class DeviceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "basic/device/%s");

    @Autowired
    private IDeviceService deviceService;

    /**
     * 设备管理首页
     * @return
     */
    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index() {
        return String.format(VM_ROOT_PATH, "index");
    }

    /**
     * 数据列表页
     * @param model
     * @param pageInfo
     * @param device
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, Device device) {

        pageInfo = deviceService.list(device, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 新增
     * @param device
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(Device device) {
        try {

            deviceService.add(device);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 根据主键获取对象
     * @param device
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Device device) {
        try {

            device = deviceService.get(device);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(device);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param device
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean modify(Device device) {
        try {

            deviceService.modify(device);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param device
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(Device device) {
        try {

            deviceService.delete(device);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
