package com.wisdom.service.service.basic;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.dao.entity.NursingSubtype;

/**
 * 护理小类
 * Created by fusj on 16/3/6.
 */
public interface INursingSubtypeService {

    /**
     * 列表数据页
     * @param nursingSubtype
     * @param pageInfo
     * @return
     */
    PageInfo list(NursingSubtype nursingSubtype, PageInfo pageInfo);

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    NursingSubtype get(Integer id);

    /**
     * 新增保存
     * @param nursingSubtype
     */
    void add(NursingSubtype nursingSubtype);

    /**
     * 修改保存
     * @param nursingSubtype
     */
    void modify(NursingSubtype nursingSubtype);

    /**
     * 根据主键删除
     * @param id
     */
    void delete(NursingSubtype nursingSubtype);
}
