package com.wisdom.service.service.basic.impl;

import com.wisdom.web.common.constants.CommonConstant;
import com.wisdom.web.common.constants.SysParamDetailConstant;
import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.util.DateUtil;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.common.util.StringUtil;
import com.wisdom.dao.entity.Device;
import com.wisdom.dao.entity.DeviceExample;
import com.wisdom.dao.mapper.DeviceMapper;
import com.wisdom.service.service.basic.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 医疗器械
 * Created by fusj on 16/3/13.
 */
@Service
@Transactional
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 分页查询
     * @param device
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo list(Device device, PageInfo pageInfo) {

        device.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);

        DeviceExample example = getCondition(device);

        if(null != pageInfo && null != pageInfo.getPageStart()) {
            example.setLimitClauseStart(pageInfo.getPageStart());
            example.setLimitClauseCount(pageInfo.getPageCount());
        }

        List<Device> list = deviceMapper.selectByExample(example);
        int totalCount = deviceMapper.countByExample(example);

        pageInfo.setList(list);
        pageInfo.setTotalCount(totalCount);

        return pageInfo;
    }

    /**
     * 新增
     * @param device
     */
    @Override
    public void add(Device device) {
        device.setSimplePinyin(Pinyin4jUtil.translate(device.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
        device.setIsDel(SysParamDetailConstant.IS_DEL_FALSE);
        device.setCreateTime(DateUtil.getTimestamp());

        deviceMapper.insertSelective(device);
    }

    /**
     * 根据主键获取对象
     * @param device
     * @return
     */
    @Override
    public Device get(Device device) {
        device = deviceMapper.selectByPrimaryKey(device.getId());

        return device;
    }

    /**
     * 修改保存
     * @param device
     */
    @Override
    public void modify(Device device) {
        device.setSimplePinyin(Pinyin4jUtil.translate(device.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));

        deviceMapper.updateByPrimaryKeySelective(device);
    }

    /**
     * 删除
     * @param device
     */
    @Override
    public void delete(Device device) {
        device.setIsDel(SysParamDetailConstant.IS_DEL_TRUE);

        deviceMapper.updateByPrimaryKeySelective(device);
    }

    /**
     * 组装查询条件
     * @param device
     * @return
     */
    private DeviceExample getCondition(Device device) {
        DeviceExample example = new DeviceExample();

        if(null != device) {
            DeviceExample.Criteria criteria = example.createCriteria();

            if(StringUtil.isNotEmptyObject(device.getIsDel())) {
                criteria.andIsDelEqualTo(device.getIsDel());
            }

            if(StringUtil.isNotEmptyObject(device.getName())) {
                criteria.andNameLike("%" + device.getName() + "%");
            }

            if(StringUtil.isNotEmptyObject(device.getSimplePinyin())) {
                criteria.andSimplePinyinLike("%" + device.getSimplePinyin() + "%");
            }
        }

        return example;
    }
}
