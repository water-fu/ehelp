package com.wisdom.service.service.sys;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.exception.ApplicationException;
import com.wisdom.dao.entity.SysParam;

import java.util.List;

/**
 * 缓存数据
 * Created by fusj on 16/3/9.
 */
public interface ISysParamService {

    /**
     * 根据Key获取对象
     * @param key
     * @return
     * @throws ApplicationException
     */
    SysParam getObjByKey(String type, String key) throws ApplicationException;

    /**
     * 根据key获取下拉列表项
     * @param key
     * @return
     * @throws ApplicationException
     */
    List<SysParam> getListByKey(String type, String key) throws ApplicationException;

    /**
     * 根据value获取中文翻译
     * @param value
     * @return
     * @throws ApplicationException
     */
    String getDescByValue(String key, String value) throws ApplicationException;

    /**
     * 一级列表数据
     * @param sysParamDetail
     * @param pageInfo
     * @return
     */
    PageInfo list(SysParam sysParamDetail, PageInfo pageInfo);

    /**
     * 新增
     * @param sysParam
     */
    void add(SysParam sysParam);

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    SysParam get(Integer id);

    /**
     * 修改
     * @param sysParam
     */
    void modify(SysParam sysParam);

    /**
     * 子项新增
     * @param sysParam
     */
    void detailAdd(SysParam sysParam);

    /**
     * 子项修改
     * @param sysParam
     */
    void detailModify(SysParam sysParam);

    /**
     * 子项删除
     * @param id
     */
    void delete(Integer id);

    /**
     * 子项列表
     * @param sysParam
     * @param pageInfo
     * @return
     */
    PageInfo detailList(SysParam sysParam, PageInfo pageInfo);
}
