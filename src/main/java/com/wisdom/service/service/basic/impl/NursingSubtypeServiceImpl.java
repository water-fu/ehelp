package com.wisdom.service.service.basic.impl;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.NursingSubtype;
import com.wisdom.dao.entity.NursingSubtypeExample;
import com.wisdom.dao.mapper.NursingSubtypeMapper;
import com.wisdom.service.service.basic.INursingSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 护理小类
 * Created by fusj on 16/3/6.
 */
@Service
@Transactional
public class NursingSubtypeServiceImpl implements INursingSubtypeService {

    @Autowired
    private NursingSubtypeMapper nursingSubtypeMapper;

    /**
     * 列表页数据
     * @param nursingSubtype
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo list(NursingSubtype nursingSubtype, PageInfo pageInfo) {

        nursingSubtype.setSimplePinyin(nursingSubtype.getSimplePinyin().toUpperCase());

        NursingSubtypeExample example = getCondition(nursingSubtype);

        if(null != pageInfo && null != pageInfo.getPageStart()) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<NursingSubtype> list = nursingSubtypeMapper.selectByExample(example);
        int totalCount = nursingSubtypeMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    @Override
    public NursingSubtype get(Integer id) {
        return nursingSubtypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增保存
     * @param nursingSubtype
     */
    @Override
    public void add(NursingSubtype nursingSubtype) {
        nursingSubtype.setSimplePinyin(Pinyin4jUtil.translate(nursingSubtype.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
        nursingSubtype.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        nursingSubtype.setCreateTime(DateUtil.getTimestamp());

        nursingSubtypeMapper.insertSelective(nursingSubtype);
    }

    /**
     * 修改保存
     * @param nursingSubtype
     */
    @Override
    public void modify(NursingSubtype nursingSubtype) {
        nursingSubtype.setSimplePinyin(Pinyin4jUtil.translate(nursingSubtype.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));

        nursingSubtypeMapper.updateByPrimaryKeySelective(nursingSubtype);
    }

    /**
     * 根据主键删除
     * @param id
     */
    @Override
    public void delete(NursingSubtype nursingSubtype) {
        nursingSubtype.setIsDel(SysParamDetailConstant.IS_DEL_TRUE);

        nursingSubtypeMapper.updateByPrimaryKeySelective(nursingSubtype);
    }

    /**
     * 拼装组装条件
     * @param nursingSubtype
     * @return
     */
    private NursingSubtypeExample getCondition(NursingSubtype nursingSubtype) {
        NursingSubtypeExample example = new NursingSubtypeExample();

        if(null != nursingSubtype) {
            NursingSubtypeExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(nursingSubtype.getSimplePinyin())) {
                criteria.andSimplePinyinLike("%" + nursingSubtype.getSimplePinyin() + "%");
            }

            if(StringUtil.isNotEmptyObject(nursingSubtype.getName())) {
                criteria.andNameLike("%" + nursingSubtype.getName() + "%");
            }

            if(StringUtil.isNotEmptyObject(nursingSubtype.getIsDel())) {
                criteria.andIsDelEqualTo(nursingSubtype.getIsDel());
            }
        }

        return example;
    }
}
