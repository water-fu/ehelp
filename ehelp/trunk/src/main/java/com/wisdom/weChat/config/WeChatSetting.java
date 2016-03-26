package com.wisdom.weChat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * wexin设置参数
 * Created by fusj on 15/12/21.
 */
@Repository
public class WeChatSetting {

    @Value("#{propertiesReader['wechat.appId']}")
    private String appId;

    @Value("#{propertiesReader['wechat.appSecret']}")
    private String appSecret;

    @Value("#{propertiesReader['wechat.token']}")
    private String token;

    @Value("#{propertiesReader['wechat.sceneStr']}")
    private String sceneStr;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public void setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
    }
}
