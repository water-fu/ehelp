package com.wisdom.service.service.basic;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.dao.entity.Device;

/**
 * 医疗器械
 * Created by fusj on 16/3/13.
 */
public interface IDeviceService {
    /**
     * 分页查询
     * @param device
     * @param pageInfo
     * @return
     */
    PageInfo list(Device device, PageInfo pageInfo);

    /**
     * 新增
     * @param device
     */
    void add(Device device);

    /**
     * 根据主键获取对象
     * @param device
     * @return
     */
    Device get(Device device);

    /**
     * 修改保存
     * @param device
     */
    void modify(Device device);

    /**
     * 删除
     * @param device
     */
    void delete(Device device);
}
