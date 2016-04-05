package com.wisdom.weChat.service.impl;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.weChat.annotation.Token;
import com.wisdom.weChat.constant.UrlConstant;
import com.wisdom.weChat.entity.AccessToken;
import com.wisdom.weChat.service.IMediaService;
import com.wisdom.weChat.util.HttpClientUtil;
import com.wisdom.web.common.constants.CommonConstant;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 多媒体文件接口
 * Created by fusj on 16/3/31.
 */
@Service
@Transactional
public class MediaServiceImpl implements IMediaService {

    @Autowired
    private SessionCache sessionCache;

    /**
     * 多媒体下载
     * @param mediaId 多媒体编号
     * @return
     */
    @Override
    @Token
    public byte[] getMediaFile(String mediaId) {
        AccessToken accessToken = (AccessToken) sessionCache.get(CommonConstant.ACCESS_TOKEN_VALUE);

        String url = UrlConstant.MEDIA_DOWN_LOAD.replace("ACCESS_TOKEN", accessToken.getToken()).replace("MEDIA_ID", mediaId);

        try {
            HttpResponse httpResponse = HttpClientUtil.doDownStr(url);

            return EntityUtils.toByteArray(httpResponse.getEntity());

        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(), ex);
        }
    }
}
