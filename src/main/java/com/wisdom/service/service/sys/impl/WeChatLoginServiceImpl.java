package com.wisdom.service.service.sys.impl;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.RequestUtil;
import com.wisdom.dao.entity.Account;
import com.wisdom.dao.entity.WeChatLogin;
import com.wisdom.dao.entity.WeChatLoginExample;
import com.wisdom.dao.mapper.AccountMapper;
import com.wisdom.dao.mapper.WeChatLoginMapper;
import com.wisdom.service.service.sys.IWeChatLoginService;
import com.wisdom.weChat.config.WeChatSetting;
import com.wisdom.weChat.util.HttpClientUtil;
import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.LoginUrlConstants;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import com.wisdom.web.entity.OAuthInfo;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * 微信登陆
 * Created by fusj on 16/4/6.
 */
@Service
@Transactional
public class WeChatLoginServiceImpl implements IWeChatLoginService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatLoginServiceImpl.class);

    @Autowired
    private WeChatSetting weChatSetting;

    @Autowired
    private WeChatLoginMapper weChatLoginMapper;

    /**
     * 微信第三方登陆
     * @param code
     * @param request
     */
    @Override
    public WeChatLogin login(String code, HttpServletRequest request) {
        try {
            // 用户授权后获取用户信息
            String url = LoginUrlConstants.LOGIN_ACCESS_TOKEN.replace("APPID", weChatSetting.getAppId())
                    .replace("SECRET", weChatSetting.getAppSecret()).replace("CODE", code);

            JSONObject jsonObject = HttpClientUtil.doGetStr(url);

            OAuthInfo oAuthInfo = new OAuthInfo();
            oAuthInfo.setAccessToken(jsonObject.getString("access_token"));
            oAuthInfo.setExpiresIn(jsonObject.getInt("expires_in"));
            oAuthInfo.setRefreshToken(jsonObject.getString("refresh_token"));
            oAuthInfo.setOpenid(jsonObject.getString("openid"));
            oAuthInfo.setScope(jsonObject.getString("scope"));

            if(jsonObject.containsKey("unionid")) {
                oAuthInfo.setUnionid(jsonObject.getString("unionid"));
            }

            logger.info("openId:" + oAuthInfo.getOpenid());
            logger.info("accessToken:" + oAuthInfo.getAccessToken());

            // 判断openId是否已经登陆过，登陆过直接获取登陆信息，未登陆，获取微信的详细信息
            WeChatLoginExample example = new WeChatLoginExample();
            example.createCriteria().andWechatIdEqualTo(oAuthInfo.getOpenid());

            // 微信登陆对象
            WeChatLogin weChatLogin;

            List<WeChatLogin> list = weChatLoginMapper.selectByExample(example);

            if(CollectionUtils.isEmpty(list)) {
                url = LoginUrlConstants.LOGIN_USER_INFO.replace("ACCESS_TOKEN", oAuthInfo.getAccessToken())
                        .replace("OPENID", oAuthInfo.getOpenid());

                jsonObject = HttpClientUtil.doGetStr(url);

                weChatLogin = new WeChatLogin();
                weChatLogin.setWechatId(jsonObject.getString("openid"));
                weChatLogin.setNickName(jsonObject.getString("nickname"));
                weChatLogin.setSex(jsonObject.getString("sex"));
                weChatLogin.setProvince(jsonObject.getString("province"));
                weChatLogin.setCity(jsonObject.getString("city"));
                weChatLogin.setCountry(jsonObject.getString("country"));
                weChatLogin.setHeadimgUrl(jsonObject.getString("headimgurl"));
                weChatLogin.setLoginIp(RequestUtil.getIP(request));
                weChatLogin.setLoginTime(DateUtil.getTimestamp());

                weChatLoginMapper.insertSelective(weChatLogin);

            } else {
                weChatLogin = list.get(0);

                weChatLogin.setLastLoginip(weChatLogin.getLoginIp());
                weChatLogin.setLastLogintime(weChatLogin.getLoginTime());
                weChatLogin.setLoginIp(RequestUtil.getIP(request));
                weChatLogin.setLoginTime(DateUtil.getTimestamp());

                weChatLoginMapper.updateByPrimaryKeySelective(weChatLogin);
            }

            return weChatLogin;

        } catch (Exception ex) {
            throw new ApplicationException("获取ACCESS_TOKEN异常", ex);
        }
    }
}
