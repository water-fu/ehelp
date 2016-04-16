package com.wisdom.service.service.user;

import com.wisdom.common.entity.SessionDetail;
import com.wisdom.dao.entity.Recover;

/**
 * 康复师
 * Created by fusj on 16/4/9.
 */
public interface IRecoverService {

    /**
     * 康复师认证(微信浏览器)
     * @param recover
     * @param headImg
     * @param bodyImg
     */
    void identification(Recover recover, byte[] headImg, byte[] bodyImg, SessionDetail sessionDetail);

    /**
     * 康复师认证(非微信浏览器)
     * @param recover
     * @param headImg
     * @param bodyImg
     */
    void identification(Recover recover, String headImg, String bodyImg, SessionDetail sessionDetail);

    /**
     * 康复师个人信息
     * @param recover
     * @param sessionDetail
     */
    void personalInfo(Recover recover, SessionDetail sessionDetail);
}
