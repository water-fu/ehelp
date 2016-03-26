package com.wisdom.mapp.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class MappSettings extends AbstractSettings {

	
//	#mapp配置
//	action.package=com.wisdom.mapp.action
//	VALID_IP=127.0.0.1,localhost,192.168.1.101,10.10.10.78,42.121.57.229,10.10.19.4,211.140.18.100
//	action.cryptPermission=false
//	encrypt.des.default.key=
//	APP_DOWNLOAD = 10.10.19.102
//	APP_REQUIRED_VERSION = 1.0
//	APP_VERSION = 1.1

	@Value("#{propertiesReader['mapp.actionPackage']}")
	private String actionPackage;

	@Value("#{propertiesReader['mapp.validIp']}")
	private String validIp;

	@Value("#{propertiesReader['mapp.encryptPermission']}")
	private boolean encryptPermission;

	@Value("#{propertiesReader['mapp.defaultKey']}")
	private String defaultKey;

	public String getActionPackage() {
		return actionPackage;
	}

	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}

	public String getValidIp() {
		return validIp;
	}

	public void setValidIp(String validIp) {
		this.validIp = validIp;
	}

	public boolean isEncryptPermission() {
		return encryptPermission;
	}

	public void setEncryptPermission(boolean encryptPermission) {
		this.encryptPermission = encryptPermission;
	}

	public String getDefaultKey() {
		return defaultKey;
	}

	public void setDefaultKey(String defaultKey) {
		this.defaultKey = defaultKey;
	}
}
