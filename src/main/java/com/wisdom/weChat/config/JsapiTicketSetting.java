package com.wisdom.weChat.config;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.weChat.entity.JsapiTicket;
import com.wisdom.weChat.service.IJsapiTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * jsapi_ticket票据申请
 * Created by fusj on 16/3/17.
 */
@Repository
public class JsapiTicketSetting {

    @Autowired
    private IJsapiTicketService jsapiTicketService;

    @Autowired
    private SessionCache sessionCache;

    /**
     * 加载时间
     */
    private Long loadTime;

    /**
     *
     */
    private int expiresIn;

    public Long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Long loadTime) {
        this.loadTime = loadTime;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void initJsapiTicket() throws Exception {
        JsapiTicket jsapiTicket = jsapiTicketService.getJsapiTicket();
        this.setExpiresIn(jsapiTicket.getExpiresIn());

        // jsapi_ticket
        sessionCache.put(CommonConstant.JSAPI_TICKET_VALUE, jsapiTicket, jsapiTicket.getExpiresIn());

        // 加载时间
        this.setLoadTime(System.currentTimeMillis());
    }
}
