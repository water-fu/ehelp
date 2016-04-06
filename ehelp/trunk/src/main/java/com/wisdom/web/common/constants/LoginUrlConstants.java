package com.wisdom.web.common.constants;

/**
 * 微信、微博、QQ登陆请求链接
 * Created by fusj on 16/4/6.
 */
public class LoginUrlConstants {

    /**
     * 微信登陆请求
     */
    public static final String WE_CHAT_LOGIN = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * oauth2登陆获取ACCESS_TOKEN
     */
    public static final String LOGIN_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * oauth2登陆获取用户信息
     */
    public static final String LOGIN_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
}
