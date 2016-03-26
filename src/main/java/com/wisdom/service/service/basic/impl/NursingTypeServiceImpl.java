package com.wisdom.service.service.basic.impl;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.NursingType;
import com.wisdom.dao.entity.NursingTypeExample;
import com.wisdom.dao.mapper.NursingSubtypeMapper;
import com.wisdom.dao.mapper.NursingTypeMapper;
import com.wisdom.service.service.basic.INursingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * 护理类型
 * Created by fusj on 16/3/6.
 */
@Service
@Transactional
public class NursingTypeServiceImpl implements INursingTypeService {

    @Autowired
    private NursingTypeMapper nursingTypeMapper;

    @Autowired
    private NursingSubtypeMapper nursingSubtypeMapper;

    /**
     * 护理大类列表页
     * @param nursingType
     * @param pageInfo
     * @return
     */
    public PageInfo list(NursingType nursingType, PageInfo pageInfo) {
        nursingType.setSimplePinyin(nursingType.getSimplePinyin().toUpperCase());
        nursingType.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);

        NursingTypeExample example = getCondition(nursingType);

        if(null != pageInfo && pageInfo.getPageStart() != null) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<NursingType> list = nursingTypeMapper.selectByExample(example);
        int totalCount = nursingTypeMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 护理大类新增
     * @param nursingType
     */
    public void add(NursingType nursingType) {
        String pinyin = Pinyin4jUtil.translate(nursingType.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR);

        nursingType.setSimplePinyin(pinyin);
        nursingType.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        nursingType.setCreateTime(DateUtil.getTimestamp());

        nursingTypeMapper.insertSelective(nursingType);
    }

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    public NursingType get(Integer id) {
        return nursingTypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存修改
     * @param nursingType
     */
    public void modify(NursingType nursingType) {
        String pinyin = Pinyin4jUtil.translate(nursingType.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR);

        nursingType.setSimplePinyin(pinyin);

        nursingTypeMapper.updateByPrimaryKeySelective(nursingType);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Integer id) {
        NursingType nursingType = new NursingType();

        nursingType.setId(id);
        nursingType.setIsDel(SysParamDetailConstant.IS_DEL_TRUE);

        nursingTypeMapper.updateByPrimaryKeySelective(nursingType);
    }

    /**
     * 拼装查询条件
     * @param nursingType
     * @return
     */
    private NursingTypeExample getCondition(NursingType nursingType) {
        NursingTypeExample example = new NursingTypeExample();

        if(null != nursingType) {
            NursingTypeExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(nursingType.getName())) {
                criteria.andNameLike("%" + nursingType.getName() + "%");
            }

            if(StringUtil.isNotEmptyObject(nursingType.getSimplePinyin())) {
                criteria.andSimplePinyinLike("%" + nursingType.getSimplePinyin() + "%");
            }

            if(StringUtil.isNotEmptyObject(nursingType.getIsDel())) {
                criteria.andIsDelEqualTo(nursingType.getIsDel());
            }
        }

        return example;
    }
}
