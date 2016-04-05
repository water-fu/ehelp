package com.wisdom.web.controller.basic;

import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.dao.entity.Zone;
import com.wisdom.service.service.basic.IZoneService;
import com.wisdom.web.common.BaseController;
import com.wisdom.web.view.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 行政区域
 * Created by fusj on 16/3/13.
 */
@Controller
@RequestMapping("zone")
public class ZoneController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ZoneController.class);

    private static final String VM_ROOT_PATH = String.format(MANAGER_VM_ROOT, "basic/zone/%s");

    @Autowired
    private IZoneService zoneService;

    /**
     * 首页
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
     * @param zone
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ModelAndView list(Model model, PageInfo pageInfo, Zone zone) {
        pageInfo = zoneService.list(zone, pageInfo);

        model.addAttribute(CommonConstant.PAGE_INFO, pageInfo);

        return new ModelAndView(String.format(VM_ROOT_PATH, "list"));
    }

    /**
     * 初始化行政区域树
     * @return
     */
    @RequestMapping(value = "initData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean initData() {
        try {
            List<Tree> list = zoneService.initData();

            ResultBean resultBean = new ResultBean(true);

            resultBean.setData(list);

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 新增
     * @param zone
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean add(Zone zone) {
        try {

            zoneService.add(zone);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 根据主键获取
     * @param zone
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Zone zone) {
        try {

            zone = zoneService.get(zone);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(zone);

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 修改保存
     * @param zone
     * @return
     */
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean modify(Zone zone) {
        try {

            zoneService.modify(zone);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 删除
     * @param zone
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(Zone zone) {
        try {

            zoneService.delete(zone);

            ResultBean resultBean = new ResultBean(true);

            return resultBean;

        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
