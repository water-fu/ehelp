package com.wisdom.service.service.sys.impl;

import com.wisdom.web.common.constants.CacheNameConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.SysParam;
import com.wisdom.dao.entity.SysParamExample;
import com.wisdom.dao.mapper.SysParamMapper;
import com.wisdom.service.service.sys.ISysParamService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 缓存数据
 * Created by fusj on 16/3/9.
 */
@Service("sysParamService")
@Transactional
public class SysParamServiceImpl implements ISysParamService {

    @Autowired
    private SysParamMapper sysParamMapper;

    /**
     * 根据Key获取对象
     * @param key
     * @return
     * @throws ApplicationException
     */
    @Cacheable(value = CacheNameConstant.PARAM_CACHE, key = "#type.concat(#key)")
    public SysParam getObjByKey(String type, String key) throws ApplicationException {
        SysParam sysParam = new SysParam();
        sysParam.setpKey(key);
        SysParamExample example = getCondition(sysParam);

        List<SysParam> list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            throw new ApplicationException("key:" + key + "不存在");
        }
        else if(list.size() > 1) {
            throw new ApplicationException("key:" + key + "存在多个");
        }

        return list.get(0);
    }

    /**
     * 根据key获取下拉列表项
     * @param key
     * @return
     * @throws ApplicationException
     */
    @Cacheable(value = CacheNameConstant.PARAM_CACHE, key = "#type.concat(#key)")
    public List<SysParam> getListByKey(String type, String key) throws ApplicationException {
        SysParam condition = new SysParam();
        condition.setpKey(key);
        SysParamExample example = getCondition(condition);

        List<SysParam> list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            throw new ApplicationException("key:" + key + "不存在");
        }
        else if(list.size() > 1) {
            throw new ApplicationException("key:" + key + "存在多个");
        }

        // 父KEY
        SysParam data = list.get(0);

        SysParam condition1 = new SysParam();
        condition1.setpId(data.getId());
        example = getCondition(condition1);

        list = sysParamMapper.selectByExample(example);

        return list;
    }

    /**
     * 根据value获取中文翻译
     * @param key
     * @param value
     * @return
     * @throws ApplicationException
     */
    @Cacheable(value = CacheNameConstant.PARAM_CACHE, key = "#key.concat(#value)")
    public String getDescByValue(String key, String value) throws ApplicationException {
        SysParam condition = new SysParam();
        condition.setpKey(key);
        SysParamExample example = getCondition(condition);

        List<SysParam> list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            throw new ApplicationException("key:" + key + "不存在");
        }
        else if(list.size() > 1) {
            throw new ApplicationException("key:" + key + "存在多个");
        }

        // 父KEY
        SysParam data = list.get(0);

        SysParam condition1 = new SysParam();
        condition1.setpId(data.getId());
        condition1.setpValue(value);
        example = getCondition(condition1);

        list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)) {
            throw new ApplicationException("value:" + value + "不存在");
        }
        else if(list.size() > 1) {
            throw new ApplicationException("value:" + value + "存在多个");
        }

        return list.get(0).getpDesc();
    }

    /**
     * 一级列表数据
     * @param sysParam
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo list(SysParam sysParam, PageInfo pageInfo) {
        SysParamExample example = new SysParamExample();
        SysParamExample.Criteria criteria = example.createCriteria();
        criteria.andPKeyIsNotNull();

        if(null != sysParam) {

            if(StringUtil.isNotEmptyObject(sysParam.getpKey())) {
                criteria.andPKeyLike("%" + sysParam.getpKey().toUpperCase() + "%");
            }

            if(StringUtil.isNotEmptyObject(sysParam.getpDesc())) {
                criteria.andPDescLike("%" + sysParam.getpDesc() + "%");
            }
        }

        if(null != pageInfo && pageInfo.getPageStart() != null) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<SysParam> list = sysParamMapper.selectByExample(example);
        int totalCount = sysParamMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 新增
     * @param sysParam
     */
    @Override
    public void add(SysParam sysParam) {
        sysParam.setpKey(sysParam.getpKey().toUpperCase());

        SysParamExample example = new SysParamExample();
        example.createCriteria().andPKeyEqualTo(sysParam.getpKey());

        List<SysParam> list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("Key:" + sysParam.getpKey() + ",重复");
        }

        sysParamMapper.insertSelective(sysParam);
    }

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    @Override
    public SysParam get(Integer id) {

        return sysParamMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改保存
     * @param sysParam
     */
    @CacheEvict(value = CacheNameConstant.PARAM_CACHE, allEntries = true)
    @Override
    public void modify(SysParam sysParam) {
        sysParam.setpKey(sysParam.getpKey().toUpperCase());

        SysParamExample example = new SysParamExample();
        example.createCriteria().andIdNotEqualTo(sysParam.getId())
                .andPKeyEqualTo(sysParam.getpKey());

        List<SysParam> list = sysParamMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("KEY:" + sysParam.getpKey() + ",重复");
        }

        sysParamMapper.updateByPrimaryKeySelective(sysParam);
    }

    /**
     * 子项新增
     * @param sysParam
     */
    @CacheEvict(value = CacheNameConstant.PARAM_CACHE, allEntries = true)
    @Override
    public void detailAdd(SysParam sysParam) {
        // pvalue重复
        SysParamExample example = new SysParamExample();
        example.createCriteria().andPValueEqualTo(sysParam.getpValue())
                .andPIdEqualTo(sysParam.getpId());

        List<SysParam> list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("值:" + sysParam.getpValue() + ",重复");
        }

        // pdesc重复
        example = new SysParamExample();
        example.createCriteria().andPDescEqualTo(sysParam.getpDesc())
                .andPIdEqualTo(sysParam.getpId());

        list = sysParamMapper.selectByExample(example);

        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("名称:" + sysParam.getpDesc() + ",重复");
        }

        sysParamMapper.insertSelective(sysParam);
    }

    /**
     * 子项修改
     * @param sysParam
     */
    @CacheEvict(value = CacheNameConstant.PARAM_CACHE, allEntries = true)
    @Override
    public void detailModify(SysParam sysParam) {
        // pvalue重复
        SysParamExample example = new SysParamExample();
        example.createCriteria().andIdNotEqualTo(sysParam.getId())
                .andPValueEqualTo(sysParam.getpValue())
                .andPIdEqualTo(sysParam.getpId());

        List<SysParam> list = sysParamMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("值:" + sysParam.getpValue() + ",重复");
        }

        // pdesc重复
        example = new SysParamExample();
        example.createCriteria().andIdNotEqualTo(sysParam.getId())
                .andPDescEqualTo(sysParam.getpDesc())
                .andPIdEqualTo(sysParam.getpId());

        list = sysParamMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)) {
            throw new ApplicationException("名称:" + sysParam.getpDesc() + ",重复");
        }

        sysParamMapper.updateByPrimaryKeySelective(sysParam);
    }

    /**
     * 子项删除
     * @param id
     */
    @CacheEvict(value = CacheNameConstant.PARAM_CACHE, allEntries = true)
    @Override
    public void delete(Integer id) {
        sysParamMapper.deleteByPrimaryKey(id);
    }

    /**
     * 子项列表
     * @param sysParam
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo detailList(SysParam sysParam, PageInfo pageInfo) {
        SysParamExample example = new SysParamExample();
        SysParamExample.Criteria criteria = example.createCriteria();
        criteria.andPIdEqualTo(sysParam.getpId());

        List<SysParam> list = sysParamMapper.selectByExample(example);
        int totalCount = sysParamMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 拼装查询条件
     * @param sysParam
     * @return
     */
    private SysParamExample getCondition(SysParam sysParam) {
        SysParamExample example = new SysParamExample();

        if(null != sysParam) {
            SysParamExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(sysParam.getpKey())) {
                criteria.andPKeyEqualTo(sysParam.getpKey());
            }

            if(StringUtil.isNotEmptyObject(sysParam.getpId())) {
                criteria.andPIdEqualTo(sysParam.getpId());
            }

            if(StringUtil.isNotEmptyObject(sysParam.getpValue())) {
                criteria.andPValueEqualTo(sysParam.getpValue());
            }

            if(StringUtil.isNotEmptyObject(sysParam.getpId())) {
                criteria.andPIdEqualTo(sysParam.getpId());
            }
        }

        return example;
    }
}
