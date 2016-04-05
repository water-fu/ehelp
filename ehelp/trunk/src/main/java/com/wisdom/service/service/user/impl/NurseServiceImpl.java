package com.wisdom.service.service.user.impl;

import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Nurse;
import com.wisdom.dao.entity.NurseExample;
import com.wisdom.dao.mapper.NurseMapper;
import com.wisdom.service.service.user.INurseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 护士详细信息
 * Created by fusj on 16/3/14.
 */
@Service
@Transactional
public class NurseServiceImpl implements INurseService {

    @Autowired
    private NurseMapper nurseMapper;

    /**
     * 根据accountId获取护士详细信息
     * @param nurse
     * @return
     */
    @Override
    public Nurse getByAccountId(Nurse nurse) {
        NurseExample example = getCondition(nurse);

        List<Nurse> list = nurseMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            return new Nurse();
        }

        return list.get(0);
    }

    private NurseExample getCondition(Nurse nurse) {
        NurseExample example = new NurseExample();

        if(null != nurse) {
            NurseExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(nurse.getAccountId())) {
                criteria.andAccountIdEqualTo(nurse.getAccountId());
            }
        }

        return example;
    }
}
