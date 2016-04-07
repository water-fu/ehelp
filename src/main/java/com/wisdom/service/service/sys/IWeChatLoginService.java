package com.wisdom.service.service.sys;

import com.wisdom.dao.entity.WeChatLogin;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信登陆
 * Created by fusj on 16/4/6.
 */
public interface IWeChatLoginService {
    /**
     * 微信第三方登陆
     * @param code
     * @param request
     */
    WeChatLogin login(String code, String type, HttpServletRequest request);
}
