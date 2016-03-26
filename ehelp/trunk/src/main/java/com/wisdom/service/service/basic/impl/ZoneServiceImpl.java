package com.wisdom.service.service.basic.impl;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Zone;
import com.wisdom.dao.entity.ZoneExample;
import com.wisdom.dao.mapper.ZoneMapper;
import com.wisdom.service.service.basic.IZoneService;
import com.wisdom.web.view.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区域
 * Created by fusj on 16/3/13.
 */
@Service
@Transactional
public class ZoneServiceImpl implements IZoneService {

    @Autowired
    private ZoneMapper zoneMapper;

    /**
     * 分页数据
     * @param zone
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo list(Zone zone, PageInfo pageInfo) {
        zone.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);

        ZoneExample example = getCondition(zone);

        if(null != pageInfo && null != pageInfo.getPageStart()) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<Zone> list = zoneMapper.selectByExample(example);
        int totalCount = zoneMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 组装查询条件
     * @param zone
     * @return
     */
    private ZoneExample getCondition(Zone zone) {
        ZoneExample example = new ZoneExample();

        if(null != zone) {
            ZoneExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(zone.getParentId())) {
                criteria.andParentIdEqualTo(zone.getParentId());
            }

            if(StringUtil.isNotEmptyObject(zone.getIsDel())) {
                criteria.andIsDelEqualTo(zone.getIsDel());
            }
        }
        return example;
    }

    /**
     * 初始化行政区域树
     * @return
     */
    @Override
    public List<Tree> initData() {

        return iterator("0");
    }

    /**
     * 新增
     * @param zone
     */
    @Override
    public void add(Zone zone) {
        Zone parentZone = zoneMapper.selectByPrimaryKey(Integer.parseInt(zone.getParentId()));

        zone.setLevel((Integer.parseInt(parentZone.getLevel()) + 1) + "");
        zone.setSimplePinyin(Pinyin4jUtil.translate(zone.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
        zone.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        zone.setCreateTime(DateUtil.getTimestamp());

        zoneMapper.insertSelective(zone);
    }

    /**
     * 根据主键获取
     * @param zone
     * @return
     */
    @Override
    public Zone get(Zone zone) {
        zone = zoneMapper.selectByPrimaryKey(zone.getId());

        return zone;
    }

    /**
     * 修改保存
     * @param zone
     */
    @Override
    public void modify(Zone zone) {
        zone.setSimplePinyin(Pinyin4jUtil.translate(zone.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));

        zoneMapper.updateByPrimaryKeySelective(zone);
    }

    /**
     * 删除
     * @param zone
     */
    @Override
    public void delete(Zone zone) {
        zone.setIsDel(SysParamDetailConstant.IS_DEL_TRUE);

        zoneMapper.updateByPrimaryKeySelective(zone);
    }

    /**
     * 递归查询行政区域树
     * @param parentId
     * @return
     */
    private List<Tree> iterator(String parentId) {
        ZoneExample example = new ZoneExample();
        example.createCriteria().andParentIdEqualTo(parentId)
                .andIsDelEqualTo(SysParamDetailConstant.IS_DEL_FALSE);
        List<Zone> parent1 = zoneMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(parent1)) {
            return null;
        }

        List<Tree> treeList = new ArrayList<>();
        for(Zone zone : parent1) {
            Tree tree = new Tree();
            tree.setId(zone.getId() + "");
            tree.setText(zone.getName());
            tree.setLevel(zone.getLevel());
            tree.setCode(zone.getCode());
            tree.setSimplePinyin(zone.getSimplePinyin());

            if(StringUtil.isNotEmptyObject(zone.getId())) {
                tree.setNodes(iterator(zone.getId().toString()));
            }

            treeList.add(tree);
        }

        return treeList;
    }
}
