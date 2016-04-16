package com.wisdom.service.service.sys.impl;

import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.JackonUtil;
import com.wisdom.common.util.RequestUtil;
import com.wisdom.dao.entity.WeChatLogin;
import com.wisdom.dao.entity.WeChatLoginExample;
import com.wisdom.dao.mapper.WeChatLoginMapper;
import com.wisdom.service.service.sys.IWeChatLoginService;
import com.wisdom.service.service.vfs.IFileService;
import com.wisdom.weChat.config.WeChatSetting;
import com.wisdom.weChat.util.HttpClientUtil;
import com.wisdom.web.common.constants.LoginUrlConstants;
import com.wisdom.web.entity.OAuthInfo;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

    @Autowired
    private IFileService fileService;

    /**
     * 微信第三方登陆
     * @param code
     * @param request
     */
    @Override
    public WeChatLogin login(String code, String type, HttpServletRequest request) {
        InputStream inputStream = null;
        try {
            // 用户授权后获取用户信息
            String url = LoginUrlConstants.LOGIN_ACCESS_TOKEN.replace("APPID", weChatSetting.getAppId())
                    .replace("SECRET", weChatSetting.getAppSecret()).replace("CODE", code);

            JSONObject jsonObject = HttpClientUtil.doGetStr(url);
            OAuthInfo oAuthInfo  = JackonUtil.readJson2Entity(jsonObject.toString(), OAuthInfo.class);

            logger.info("openId:" + oAuthInfo.getOpenid());
            logger.info("accessToken:" + oAuthInfo.getAccess_token());

            // 判断openId是否已经登陆过，登陆过直接获取登陆信息，未登陆，获取微信的详细信息
            WeChatLoginExample example = new WeChatLoginExample();
            example.createCriteria().andWechatIdEqualTo(oAuthInfo.getOpenid());

            // 微信登陆对象
            WeChatLogin weChatLogin;

            List<WeChatLogin> list = weChatLoginMapper.selectByExample(example);

            if(CollectionUtils.isEmpty(list)) {

                url = LoginUrlConstants.LOGIN_USER_INFO.replace("ACCESS_TOKEN", oAuthInfo.getAccess_token())
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
                weChatLogin.setAccountType(type);
                weChatLogin.setLoginIp(RequestUtil.getIP(request));
                weChatLogin.setLoginTime(DateUtil.getTimestamp());

                // 把头像上传到VFS
                URL headUrl = new URL(weChatLogin.getHeadimgUrl());
                HttpURLConnection connection = (HttpURLConnection) headUrl.openConnection();
                inputStream = connection.getInputStream();

                String fileId = fileService.uploadFile(fileService.getServerConfig(), inputStream, "jpg", weChatLogin.getWechatId(), 999);
                weChatLogin.setHeadimgUrl(fileId);

                weChatLoginMapper.insertSelective(weChatLogin);

            } else {
                weChatLogin = list.get(0);

                // 如果不是同一个类型，就表示已经注册其他角色
                if(!weChatLogin.getAccountType().equals(type)) {
                    throw new ApplicationException("该微信号已经注册其他角色");
                }

                weChatLogin.setLastLoginip(weChatLogin.getLoginIp());
                weChatLogin.setLastLogintime(weChatLogin.getLoginTime());
                weChatLogin.setLoginIp(RequestUtil.getIP(request));
                weChatLogin.setLoginTime(DateUtil.getTimestamp());

                weChatLoginMapper.updateByPrimaryKeySelective(weChatLogin);
            }

            return weChatLogin;

        } catch (Exception ex) {
            throw new ApplicationException("微信登陆异常:" + ex.getMessage(), ex);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
