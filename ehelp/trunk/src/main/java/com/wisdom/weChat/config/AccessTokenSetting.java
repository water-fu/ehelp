package com.wisdom.weChat.config;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.weChat.entity.AccessToken;
import com.wisdom.weChat.service.IAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 获取AccessToken
 * Created by fusj on 15/12/21.
 */
@Repository
public class AccessTokenSetting {

    @Autowired
    private IAccessTokenService accessTokenService;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private JsapiTicketSetting jsapiTicketSetting;

    /**
     * 加载时间
     */
    private Long loadTime;

    /**
     * 过期时间
     */
    private int expiresIn;

    /**
     * 是否正在加载
     */
    private boolean isLoading;

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

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * 项目启动初始化access_token
     * @throws Exception
     */
    @PostConstruct
    public void initAccessToken() throws Exception {
        this.setLoadTime(System.currentTimeMillis());

        AccessToken accessToken = accessTokenService.getAccessToken();
        this.setExpiresIn(accessToken.getExpiresIn());

        // 把accessToken放到redis中
        sessionCache.put(CommonConstant.ACCESS_TOKEN_VALUE, accessToken, accessToken.getExpiresIn());

        // 初始化jsapi_ticket
        jsapiTicketSetting.initJsapiTicket();
    }
}
