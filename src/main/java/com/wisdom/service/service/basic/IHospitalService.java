package com.wisdom.service.service.basic;

import com.wisdom.dao.entity.Hospital;
import com.wisdom.common.entity.PageInfo;

/**
 * 医院接口
 * Created by fusj on 16/3/2.
 */
public interface IHospitalService {

    /**
     * 列表数据
     * @param hospital
     * @param pageInfo
     * @return
     */
    PageInfo list(Hospital hospital, PageInfo pageInfo);

    /**
     * 新增医院
     * @param hospital
     * @return
     */
    int addHospital(Hospital hospital);

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    Hospital get(Integer id);

    /**
     * 保存修改
     * @param hospital
     */
    void editHospital(Hospital hospital);

    /**
     * 删除
     * @param id
     */
    void deleteHospital(Integer id);
}
