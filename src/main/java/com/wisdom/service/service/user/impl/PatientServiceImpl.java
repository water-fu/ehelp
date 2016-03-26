package com.wisdom.service.service.user.impl;

import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.dao.entity.Patient;
import com.wisdom.dao.entity.PatientExample;
import com.wisdom.dao.mapper.PatientMapper;
import com.wisdom.service.service.user.IPatientService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 患者实名信息
 * Created by fusj on 16/3/14.
 */
@Service
@Transactional
public class PatientServiceImpl implements IPatientService {

    @Autowired
    private PatientMapper patientMapper;

    /**
     * 根据accountId获取患者实名信息
     * @param patient
     * @return
     */
    @Override
    public Patient getAuditByAccountId(Patient patient) {
        PatientExample example = new PatientExample();
        example.createCriteria().andAccountIdEqualTo(patient.getAccountId())
                .andRelationEqualTo(SysParamDetailConstant.RELATION_MYSELF);

        List<Patient> list = patientMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            return new Patient();
        }

        return list.get(0);
    }
}
