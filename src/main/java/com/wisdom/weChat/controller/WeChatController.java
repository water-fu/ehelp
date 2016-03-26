package com.wisdom.weChat.controller;

import com.wisdom.common.entity.ResultBean;
import com.wisdom.weChat.config.WeChatSetting;
import com.wisdom.weChat.entity.JsapiTicket;
import com.wisdom.weChat.service.IJsapiTicketService;
import com.wisdom.weChat.util.WeChatUtil;
import com.wisdom.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信首页处理器
 * Created by fusj on 16/3/17.
 */
@Controller
@RequestMapping("weChat")
public class WeChatController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private WeChatSetting weChatSetting;

    @Autowired
    private IJsapiTicketService jsapiTicketService;

    /**
     * 微信公众号GET请求
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String doGet(HttpServletRequest request) throws Exception {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        if(WeChatUtil.checkSignature(signature, timestamp, nonce, weChatSetting.getToken())){
            return echostr;
        }

        return "";
    }

    /**
     * 微信公众号POST请求
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(HttpServletRequest request) throws Exception {

        return "";
    }

    /**
     * 获取微信config时的参数
     * @return
     */
    @RequestMapping(value = "getWeChatConfigParam", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getWeChatConfigParam(String url) {
        try {
            Map map = jsapiTicketService.sign(url);
            map.put("appId", weChatSetting.getAppId());

            // 需要获取接口权限
            List<String> jsApiList = new ArrayList<>();
            jsApiList.add("onMenuShareTimeline");
            jsApiList.add("hideOptionMenu");
            jsApiList.add("showMenuItems");

            map.put("jsApiList", jsApiList);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(map);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }
}
