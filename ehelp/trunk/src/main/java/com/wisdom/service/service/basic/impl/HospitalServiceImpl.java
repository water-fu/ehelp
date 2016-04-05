package com.wisdom.service.service.basic.impl;

import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Hospital;
import com.wisdom.dao.entity.HospitalExample;
import com.wisdom.dao.mapper.HospitalMapper;
import com.wisdom.service.service.basic.IHospitalService;
import com.wisdom.common.entity.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 医院接口
 * Created by fusj on 16/3/2.
 */
@Service
@Transactional
public class HospitalServiceImpl implements IHospitalService {

    @Autowired
    private HospitalMapper hospitalMapper;

    /**
     * 列表数据
     * @param hospital
     * @param pageInfo
     * @return
     */
    public PageInfo list(Hospital hospital, PageInfo pageInfo) {

        hospital.setIsDel("0");
        HospitalExample example = getCondition(hospital);

        if(null != pageInfo && pageInfo.getPageStart() != null) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<Hospital> list = hospitalMapper.selectByExample(example);
        int totalCount = hospitalMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 医院新增
     * @param hospital
     * @return
     */
    public int addHospital(Hospital hospital) {

        hospital.setSimplePinyin(Pinyin4jUtil.translate(hospital.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
        hospital.setIsDel("0");
        hospital.setCreateTime(new Timestamp(new Date().getTime()));

        hospitalMapper.insertSelective(hospital);

        return hospital.getId();
    }

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    public Hospital get(Integer id) {

        Hospital hospital = hospitalMapper.selectByPrimaryKey(id);

        return hospital;
    }

    /**
     * 保存修改
     * @param hospital
     */
    public void editHospital(Hospital hospital) {
        hospitalMapper.updateByPrimaryKeySelective(hospital);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteHospital(Integer id) {
        Hospital hospital = new Hospital();

        hospital.setId(id);
        hospital.setIsDel("1");

        hospitalMapper.updateByPrimaryKeySelective(hospital);
    }

    /**
     * 组装查询条件
     * @param hospital
     * @return
     */
    private HospitalExample getCondition(Hospital hospital) {
        HospitalExample example = new HospitalExample();

        if(null != hospital) {
            HospitalExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(hospital.getName())) {
                criteria.andNameLike(hospital.getName());
            }

            if(StringUtil.isNotEmptyObject(hospital.getSimplePinyin())) {
                criteria.andSimplePinyinLike(hospital.getSimplePinyin());
            }

            if(StringUtils.isNotBlank(hospital.getIsDel())) {
                criteria.andIsDelEqualTo(hospital.getIsDel());
            }

        }

        example.setOrderByClause(" simple_pinyin asc ");

        return example;
    }
}
