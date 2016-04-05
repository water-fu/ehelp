package com.wisdom.mapp.core.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * DESede对称加密算法演示
 * */
public class DESedeUtils {
	/**
	 * 密钥算法
	 * */
	public static final String KEY_ALGORITHM="DESede";
	
	/**
	 * 加密/解密算法/工作模式/填充方式
	 * */
	public static final String CIPHER_ALGORITHM="DESede/ECB/PKCS5Padding";
	
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		//还原密钥
		//实例化密钥生成器
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		//初始化密钥生成器
		kg.init(168);
		//生成密钥
		SecretKey secretKey=kg.generateKey();
		//获取二进制密钥编码形式
		byte[] key = secretKey.getEncoded();
		
		//实例化Des密钥
		DESedeKeySpec dks=new DESedeKeySpec(key);
		//实例化密钥工厂
		SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);
		//生成密钥
		SecretKey k=keyFactory.generateSecret(dks);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		//初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 
	 * 生成密钥
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initkey() throws Exception{
		
		//实例化密钥生成器
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		//初始化密钥生成器
		kg.init(168);
		//生成密钥
		SecretKey secretKey=kg.generateKey();
		//获取二进制密钥编码形式
		return secretKey.getEncoded();
	}
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return Key 密钥
	 * */
	public static Key toKey(byte[] key) throws Exception{
		//实例化Des密钥
		DESedeKeySpec dks=new DESedeKeySpec(key);
		//实例化密钥工厂
		SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);
		//生成密钥
		SecretKey secretKey=keyFactory.generateSecret(dks);
		return secretKey;
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		//还原密钥
		Key k=toKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		//初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//执行操作
		return byte2hex(cipher.doFinal(data)).getBytes("utf-8");
	}
	/**
	 * 解密数据
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		
		data = hex2byte(data);
		
		Security.addProvider(new BouncyCastleProvider());
		//欢迎密钥
		Key k =toKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		//初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	
	
	/**
	 * 
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 */

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;

		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	
	/**
	 * 进行加解密的测试
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		
		String str="DESede加密测试&éï⋯⋯%￥#";
		
		
//		System.out.println(Base64.encodeBase64String("辉哥解密成功ok！@￥".getBytes()));
		
//		DESedeUtils.encrypt(str.getBytes());
		
//		String key="ailk_reliance_1234567890";
		System.out.println("原文："+str);
		//初始化密钥
		byte[] key= "ailk_reliance_1234567890".getBytes();
		System.out.println("密钥："+Base64.encodeBase64String(key));
		//加密数据
		byte[] data=DESedeUtils.encrypt(str.getBytes(), key);
		
		String b64 = Base64.encodeBase64String(data);
		
//		b64 = "2mMlrERIvxHdLO9H0y+uRnOQjRL9vF1xbTUczPaMRBwT692zWWO7X7QiS8IlEe2A96KptGJZ4mA=";
		
		System.out.println("加密后："+b64);
		
		System.out.println(Base64.decodeBase64(b64));
		
		data = Base64.decodeBase64(b64);
		
		
		//解密数据
		data=DESedeUtils.decrypt(data, key);
		System.out.println("解密后："+new String(data));
	}
}
//控制台输出结果：
//原文：	DESede
//密钥：	BBDmwTjBsF7IwTIyGWt1bmFntRyUgMQL
//加密后：	FM/DsEv3KgM=
//解密后：	DESede
