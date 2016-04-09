package com.wisdom.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * 系统参数
 * Created by fusj on 16/4/7.
 */
@Repository
public class SystemSetting {

    /**
     * 是否生产环境
     */
    @Value("#{propertiesReader['system.productionMode']}")
    private Boolean productionMode;

    public Boolean getProductionMode() {
        return productionMode;
    }

    public void setProductionMode(Boolean productionMode) {
        this.productionMode = productionMode;
    }
}
