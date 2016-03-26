package com.wisdom.common.toolbox;

import com.wisdom.common.constants.CommonConstant;
import com.wisdom.common.constants.SysParamDetailConstant;
import com.wisdom.common.util.SpringBeanUtil;
import com.wisdom.dao.entity.SysParam;
import com.wisdom.service.service.sys.ISysParamService;

import java.util.List;

/**
 * 缓存数据工具类
 * Created by fusj on 16/3/9.
 */
public class SysParamTools {

    private static final String SERVICE_NAME = "sysParamService";

    /**
     * 根据KEY获取对象
     * @param key
     * @return
     */
    public static SysParam getObjByKey(String key) {
        ISysParamService sysParamService = (ISysParamService) SpringBeanUtil.getSpringBean(SERVICE_NAME);

        return sysParamService.getObjByKey(CommonConstant.SINGLE_TYPE, key);
    }

    /**
     * 根据KEY获取下拉列表
     * @param key
     * @return
     */
    public static List<SysParam> getListByKey(String key) {
        ISysParamService sysParamService = (ISysParamService) SpringBeanUtil.getSpringBean(SERVICE_NAME);

        return sysParamService.getListByKey(CommonConstant.LIST_TYPE, key);
    }

    /**
     * 根据KEY,VALUE获取中文翻译
     * @param key
     * @param value
     * @return
     */
    public static String getDescByValue(String key, String value) {
        ISysParamService sysParamService = (ISysParamService) SpringBeanUtil.getSpringBean(SERVICE_NAME);

        return sysParamService.getDescByValue(key, value);
    }
}
