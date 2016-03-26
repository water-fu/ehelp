package com.wisdom.service.service.user;

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
}
