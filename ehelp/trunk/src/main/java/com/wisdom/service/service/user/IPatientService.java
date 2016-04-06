package com.wisdom.service.service.user;

import com.wisdom.common.entity.SessionDetail;
import com.wisdom.dao.entity.Patient;

/**
 * 患者实名信息
 * Created by fusj on 16/3/14.
 */
public interface IPatientService {
    /**
     * 根据accountId获取患者实名信息
     * @param patient
     * @return
     */
    Patient getAuditByAccountId(Patient patient);

    /**
     * 患者认证(微信浏览器)
     * @param patient
     * @param headImg
     * @param bodyImg
     */
    void identification(Patient patient, byte[] headImg, byte[] bodyImg, SessionDetail sessionDetail);

    /**
     * 患者认证(非微信浏览器)
     * @param patient
     * @param headImg
     * @param bodyImg
     */
    void identification(Patient patient, String headImg, String bodyImg, SessionDetail sessionDetail);
}
