package com.wisdom.common.encrypt;

import com.wisdom.common.exception.ApplicationException;

/**
 * 加密工厂
 * @author
 *
 */
public class EncryptFactory {

	/**
	 * 根据摘要算法字符串返回加密类
	 * 支持MD5算法
	 * @param encryptName
	 * @return
	 * @throws
	 */
	public static IEncrypt getInstance(String encryptName) throws ApplicationException{
		IEncrypt iEncrypt = null;
		try {
			iEncrypt = (IEncrypt) Class.forName("com.wisdom.common.encrypt."+encryptName).newInstance();
		} catch (Exception e) {
			throw new ApplicationException("com.wisdom.common.encrypt."+encryptName+"类初始化异常", e);
		}

		return iEncrypt;
	}
}
