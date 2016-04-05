package com.wisdom.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * VFS配置文件
 * Created by fusj on 16/3/16.
 */
@Repository
public class VfsSetting {
    /**
     * 服务器名称
     */
    @Value("#{propertiesReader['vfs.serverName']}")
    private String serverName;

    /**
     * 加密
     */
    private String desKey;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }
}
