package com.wisdom.service.service.vfs;

import com.wisdom.dao.entity.vfs.FileServerConfig;
import com.wisdom.dao.entity.vfs.FileUploadRecord;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IFileService {
	
	
	
	public void insertTest(FileUploadRecord record) throws Exception;
	/**
	 * 通过服务器名称获取服务器配置相关信息
	 * @param serverName
	 * @return
	 * @throws Exception
	 */
	public FileServerConfig getServerConfig(String serverName) throws Exception;

	public FileServerConfig getServerConfig() throws Exception;
	
	
	/**
	 * 场景：用于外服务下载文件时，通过aip到woego内部下载文件后，上传回原应用
	 * @param fileServerConfig
	 * @param fileid 获取的文件ID
	 * @param extName 扩展名
	 * @param path 回传的路径
	 * @param is
	 * @return
	 */
	public String downloadAndUploadFile(FileServerConfig fileServerConfig, String fileid, String extName, String path, InputStream is, String fileName) throws Exception;
	
	
	/**
	 * 
	 * @param fileServerConfig
	 * @param contents
	 * @param extName
	 * @param fileName
	 * @param isTemp
	 * @param needValidate
	 * @return
	 */
	public String uploadFile(FileServerConfig fileServerConfig,
							 byte[] contents, String extName, String fileName,
							 Integer userid, boolean isTemp, boolean needValidate)
			throws Exception;

	/**
	 * 
	 * @param fileServerConfig
	 * @param is
	 *            外部送入流
	 * @param extName
	 *            文件扩展名
	 * @param fileName
	 *            文件名称
	 * @param isTemp
	 *            是否上传到临时文件夹
	 * @param needValidate
	 *            权限标示；false:无权限；true:有权限
	 * @return
	 */
	public String uploadFile(FileServerConfig fileServerConfig, InputStream is,
							 String extName, String fileName, Integer userid,
							 boolean isTemp, boolean needValidate) throws Exception;

	/**
	 * 把流上传到文件服务器，默认按无权限模式进行上传
	 * 
	 * @param fileServerConfig
	 * @param is
	 * @param extName
	 * @param fileName
	 * @return
	 */
	public String uploadFile(FileServerConfig fileServerConfig, InputStream is,
							 String extName, String fileName, Integer userid)
			throws Exception;

	/**
	 * 把文件上传到文件服务器，默认按无权限模式进行上传
	 * 
	 * @param fileServerConfig
	 *            文件服务器信息
	 * @param file
	 *            文件
	 * @param extName
	 *            扩展名
	 * @return 返回fileid
	 */
	public String uploadFile(FileServerConfig fileServerConfig, File file,
							 String extName, Integer userid) throws Exception;

	/**
	 * 把文件上传到文件服务器
	 * 
	 * @param fileServerConfig
	 * 
	 * @param file
	 *            文件
	 * @param extName
	 *            扩展名
	 * @param isTemp
	 *            是否上传临时文件夹
	 * @param needValidate
	 *            权限标示；false:无权限；true:有权限
	 * @return 返回fileid
	 */
	public String uploadFile(FileServerConfig fileServerConfig, File file,
							 String extName, Integer userid, boolean isTemp,
							 boolean needValidate) throws Exception;

	/**
	 * 从文件服务器下载文件
	 * 
	 * @return 返回流对象InputStream
	 */
	public InputStream downloadFile(String fileid, Integer userid, FileServerConfig fileServerConfig)
			throws Exception;

	/**
	 * 从文件服务器删除文件
	 * 
	 */
	public void removeFile(String fileid, Integer userid, FileServerConfig fileServerConfig)
			throws Exception;

	/**
	 * 根据传入fileid返回文件http访问地址；
	 * 
	 */
	public String getHttpUrl(String fileid, FileServerConfig fileServerConfig) throws Exception;
	public String getHttpUrl(String fileid) throws Exception;

	/**
	 * 把文件从临时目录转移正式
	 * 
	 */
	public void tranFile(String fileid, Integer userid, FileServerConfig fileServerConfig)
			throws Exception;

	/**
	 * <p>
	 * 描述:批量获得文件访问路径
	 * </p>
	 * 
	 */
	public Map<String, String> getHttpUrls(List<String> fileids, FileServerConfig fileServerConfig)
			throws Exception;

	/**
	 * 获取远端服务器文件，本地上传到另外的服务器
	 * @param remoteUri  sftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz
	 * @param localUri   ftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz
	 */
	public void downloadByUri(String remoteUri, String localUri)
			throws Exception;

}
