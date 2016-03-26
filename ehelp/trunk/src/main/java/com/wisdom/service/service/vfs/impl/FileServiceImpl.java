package com.wisdom.service.service.vfs.impl;

import com.google.common.util.concurrent.AtomicDouble;
import com.wisdom.common.config.VfsSetting;
import com.wisdom.dao.entity.vfs.FileServerConfig;
import com.wisdom.dao.entity.vfs.FileUploadInfo;
import com.wisdom.dao.entity.vfs.FileUploadInfoExample;
import com.wisdom.dao.entity.vfs.FileUploadRecord;
import com.wisdom.dao.mapper.vfs.FileServerConfigMapper;
import com.wisdom.dao.mapper.vfs.FileUploadInfoMapper;
import com.wisdom.dao.mapper.vfs.FileUploadRecordMapper;
import com.wisdom.service.service.vfs.IFileService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class FileServiceImpl implements IFileService {

	private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

	private static final Integer OPTR_ADD = 0;// 添加
	private static final Integer OPTR_DEL = 1;// 删除
	private static final Integer OPTR_DOWLOAD = 2;// 下载
	private static final Integer OPTR_TRANS = 2;// 从临时文件转成正式文件

	private static final AtomicDouble ad = new AtomicDouble(95);
	
	@Autowired
	private VfsSetting vfsSetting;
	
	@Autowired
	private FileUploadInfoMapper fileUploadInfoMapper;

	@Autowired
	private FileUploadRecordMapper fileUploadRecordMapper;
	
	@Autowired
	private FileServerConfigMapper fileServerConfigMapper;

	public void insertTest(FileUploadRecord record) throws Exception
	{
		fileUploadRecordMapper.insert(record);
		throw new Exception("vfs1 异常");
	}
	
	public FileServerConfig getServerConfig(String serverName) throws Exception
	{
	return fileServerConfigMapper.selectByPrimaryKey(serverName);
	}
	
	public FileServerConfig getServerConfig() throws Exception
	{
		if(vfsSetting.getServerName() == null)
		{
			return null;
		}
		
		return getServerConfig(vfsSetting.getServerName());
	}
	
	@Override
	public String uploadFile(FileServerConfig fileServerConfig, byte[] contents, String extName, String fileName, Integer userid, boolean isTemp, boolean needValidate) throws Exception {
		// 根据规则生成fileid
		String fileid = genFileId(needValidate);
		// 按天建目录
		String path = fileid.substring(1, 9);
		if (isTemp) {
			path = "temp/" + path;
		}

		// 根据规则生成uri
		FileObject vfsFile = null;
		OutputStream out = null;

		try {
			String uri = genVFSURI(fileid, extName, path, fileServerConfig);
			FileSystemManager fsManager = VFS.getManager();
			
			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				vfsFile = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				vfsFile = fsManager.resolveFile(uri);
			}

			if (!vfsFile.exists()) {
				vfsFile.createFile();
			}
			out = vfsFile.getContent().getOutputStream();
			out.write(contents, 0, contents.length);

			out.flush();
		} catch (FileSystemException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {

			try {
				if (out != null)
					out.close();
			} catch (Exception e) {

			}
			try {
				if (vfsFile != null)
					vfsFile.close();

			} catch (Exception e) {

			}
		}

		// 操作数据库
		String fileExt = extName;
		if (StringUtils.isEmpty(extName) && fileName.indexOf(".") > 0) {
			fileExt = fileName.substring(fileName.indexOf(".") + 1);
		}
		// 记录file_upload_info
		FileUploadInfo fileInfo = new FileUploadInfo();
		fileInfo.setFileName(fileName);
		fileInfo.setFileId(fileid);
		fileInfo.setFileExt(fileExt);
		fileInfo.setFileSize((long) contents.length);
		fileInfo.setFileServer(fileServerConfig.getServerName());
		fileInfo.setFilePath(String.format("%s/%s", fileServerConfig.getRootPath(), path));

		//fileInfo.setFilePath(path);
		fileInfo.setUserId(userid);
		fileInfo.setCreateTime(new Timestamp(new Date().getTime()));
		fileUploadInfoMapper.insert(fileInfo);

		// 记录file_upload_record
		FileUploadRecord record = new FileUploadRecord();
		record.setFileId(fileid);
		record.setFileName(fileName);
		record.setFileServer(fileServerConfig.getServerName());
		record.setFilePath(String.format("%s/%s", fileServerConfig.getRootPath(), path));

		//record.setFilePath(path);
		record.setOptrType(OPTR_ADD);
		record.setUserId(userid);
		record.setOptrDate(new Timestamp(new Date().getTime()));
		fileUploadRecordMapper.insert(record);
		return fileid;
	}

	@Override
	public String uploadFile(FileServerConfig fileServerConfig, InputStream is, String extName, String fileName, Integer userid, boolean isTemp, boolean needValidate) throws Exception {
		long fileSize = 0;
		// 根据规则生成fileid
		String fileid = genFileId(needValidate);

		// 按天建目录
		String path = fileid.substring(1, 9);
		;
		if (isTemp) {
			path = "temp/" + path;
		}

		// 根据规则生成uri

		FileObject vfsFile = null;

		OutputStream out = null;

		try {
			String uri = genVFSURI(fileid, extName, path, fileServerConfig);
			FileSystemManager fsManager = VFS.getManager();

			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				vfsFile = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				vfsFile = fsManager.resolveFile(uri);
			}

			if (!vfsFile.exists()) {
				vfsFile.createFile();
			}
			byte content[] = new byte[1024];
			int len = 0;
			out = vfsFile.getContent().getOutputStream();
			while ((len = is.read(content)) != -1) {
				fileSize += len;
				out.write(content, 0, len);
			}
			out.flush();
		} catch (FileSystemException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			try {
				if (out != null)
					out.close();
			} catch (Exception e) {

			}
			try {
				if (vfsFile != null)
					vfsFile.close();

			} catch (Exception e) {

			}
		}

		// 操作数据库
		String fileExt = extName;
		if (StringUtils.isEmpty(extName) && fileName.indexOf(".") > 0) {
			fileExt = fileName.substring(fileName.indexOf(".") + 1);
		}
		// 记录file_upload_info
		FileUploadInfo fileInfo = new FileUploadInfo();
		fileInfo.setFileName(fileName);
		fileInfo.setFileId(fileid);
		fileInfo.setFileExt(fileExt);
		fileInfo.setFileSize(fileSize);
		fileInfo.setFileServer(fileServerConfig.getServerName());
		fileInfo.setFilePath(String.format("%s/%s", fileServerConfig.getRootPath(), path));
		fileInfo.setUserId(userid);
		fileInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		fileUploadInfoMapper.insert(fileInfo);

		// 记录file_upload_record
		FileUploadRecord record = new FileUploadRecord();
		record.setFileId(fileid);
		record.setFileName(fileName);
		record.setFileServer(fileServerConfig.getServerName());
		record.setFilePath(String.format("%s/%s", fileServerConfig.getRootPath(), path));
		record.setOptrType(OPTR_ADD);
		record.setUserId(userid);
		record.setOptrDate(new Timestamp(System.currentTimeMillis()));
		fileUploadRecordMapper.insert(record);
		return fileid;
	}

	@Override
	public String uploadFile(FileServerConfig fileServerConfig, InputStream is, String extName, String fileName, Integer userid) throws Exception {
		return uploadFile(fileServerConfig, is, extName, fileName, userid, false, false);
	}

	@Override
	public String uploadFile(FileServerConfig fileServerConfig, File file, String extName, Integer userid) throws Exception {
		return uploadFile(fileServerConfig, file, extName, userid, false, false);

	}

	@Override
	public String uploadFile(FileServerConfig fileServerConfig, File file, String extName, Integer userid, boolean isTemp, boolean needValidate) throws Exception {
		InputStream is = null;
		String result = null;
		try {
			try {
				is = new FileInputStream(file);
				result = uploadFile(fileServerConfig, is, extName, file.getName(), userid, isTemp, needValidate);
			} catch (FileNotFoundException e) {
				throw e;
			}

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	@Override
	public InputStream downloadFile(String fileid, Integer userid, FileServerConfig fileServerConfig) throws Exception {
		/**
		 * 如果需要可以在这个地方增加权限校验，判断是否能够查看这个文件
		 */
		FileUploadInfo fileUploadInfo = fileUploadInfoMapper.selectByPrimaryKey(fileid);

		if (fileUploadInfo == null) {
			throw new Exception("无文件记录");
		}
		FileObject vfsFile = null;
		try {
			String uri = genVFSURI(fileid, fileUploadInfo.getFileExt(), fileUploadInfo.getFilePath(), fileServerConfig);
			FileSystemManager fsManager = VFS.getManager();

			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				vfsFile = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				vfsFile = fsManager.resolveFile(uri);
			}

			if (!vfsFile.exists()) {
				throw new Exception("文件不存在");
			}
			return vfsFile.getContent().getInputStream();

		} catch (FileSystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (vfsFile != null) {
					vfsFile.close();
					// 记录file_upload_record
					FileUploadRecord record = new FileUploadRecord();
					record.setFileId(fileid);
					record.setFileName(fileUploadInfo.getFileName());
					record.setFilePath(fileUploadInfo.getFilePath());
					record.setFileServer(fileUploadInfo.getFileServer());
					record.setOptrType(OPTR_DOWLOAD);
					record.setUserId(userid);

					record.setOptrDate(new Timestamp(new Date().getTime()));
					fileUploadRecordMapper.insert(record);

				}
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void removeFile(String fileid, Integer userid, FileServerConfig fileServerConfig) throws Exception {
		FileUploadInfo fileUploadInfo = fileUploadInfoMapper.selectByPrimaryKey(fileid);

		if (fileUploadInfo == null) {
			throw new Exception("待删除文件文件不存在！");
		}
		// 如果只有文件创建人能够删除，则放开以下代码
		/*
		 * if (fileUploadInfo.getUserId() != userInfo.getUserId()){ throw new
		 * BusinessException("00000","你没有权限删除该文件"); }
		 */

		FileObject vfsFile = null;
		try {
			String uri = genVFSURI(fileid, fileUploadInfo.getFileExt(), fileUploadInfo.getFilePath(), fileServerConfig);
			FileSystemManager fsManager = VFS.getManager();

			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				vfsFile = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				vfsFile = fsManager.resolveFile(uri);
			}

			if (vfsFile.exists()) {
				vfsFile.delete();
				// 删除file_upload_info
				fileUploadInfoMapper.deleteByPrimaryKey(fileid);

				// 记录file_upload_record
				FileUploadRecord record = new FileUploadRecord();
				record.setFileId(fileid);
				record.setFileName(fileUploadInfo.getFileName());
				record.setFilePath(fileUploadInfo.getFilePath());
				record.setFileServer(fileUploadInfo.getFileServer());
				record.setOptrType(OPTR_DEL);
				record.setUserId(userid);

				record.setOptrDate(new Timestamp(new Date().getTime()));
				fileUploadRecordMapper.insert(record);

			} else {
				throw new Exception("文件不存在");

			}
		} catch (FileSystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				vfsFile.close();
			} catch (Exception e) {

			}
		}
	}

	@Override
	public String getHttpUrl(String fileid, FileServerConfig fileServerConfig) throws Exception {
		FileUploadInfo fileUploadInfo = fileUploadInfoMapper.selectByPrimaryKey(fileid);

		if (fileUploadInfo == null) {
			throw new Exception("文件不存在");
		}
		if (fileServerConfig == null) {
			fileServerConfig = getServerConfig();
		}
		
//		if (fileUploadInfo.getFilePath().startsWith(fileServerConfig.getRootPath())) {
//			fileUploadInfo.setFilePath(fileUploadInfo.getFilePath().replace(fileServerConfig.getRootPath() + "/", ""));
//		}
		return getHttpUrlByFileObject(fileUploadInfo, fileServerConfig);
	}

	@Override
	public void tranFile(String fileid, Integer userid, FileServerConfig fileServerConfig) throws Exception {
		FileUploadInfo fileUploadInfo = fileUploadInfoMapper.selectByPrimaryKey(fileid);

		if (fileUploadInfo == null) {
			throw new Exception("文件不存在");

		}

		FileObject srcVfsFile = null;
		FileObject destVfsFile = null;
		try {
			String srcUri = genVFSURI(fileid, fileUploadInfo.getFileExt(), fileUploadInfo.getFilePath(), fileServerConfig);

			String destUri = genVFSURI(fileid, fileUploadInfo.getFileExt(), fileid.substring(1, 9), fileServerConfig);

			FileSystemManager fsManager = VFS.getManager();

			srcVfsFile = fsManager.resolveFile(srcUri);
			destVfsFile = fsManager.resolveFile(destUri);

			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				srcVfsFile = fsManager.resolveFile(srcUri, fileSystemOptions);
				destVfsFile = fsManager.resolveFile(destUri, fileSystemOptions);
			} else {
				srcVfsFile = fsManager.resolveFile(srcUri);
				destVfsFile = fsManager.resolveFile(destUri);
			}

			if (!srcVfsFile.exists()) {
				throw new Exception("文件不存在");

			}

			destVfsFile.moveTo(srcVfsFile);

		} catch (FileSystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (srcVfsFile != null)
					srcVfsFile.close();
			} catch (Exception e) {

			}
			try {
				if (destVfsFile != null)
					destVfsFile.close();
			} catch (Exception e) {

			}
		}

		// 记录file_upload_info
		FileUploadInfo fileInfo = new FileUploadInfo();
		fileInfo.setFileName(fileUploadInfo.getFileName());
		fileInfo.setFileId(fileid);
		fileInfo.setFileExt(fileUploadInfo.getFileExt());
		fileInfo.setFileSize(fileUploadInfo.getFileSize());
		fileInfo.setFileServer(fileUploadInfo.getFileServer());
		fileInfo.setFilePath(fileid.substring(1, 9));
		fileInfo.setUserId(userid);
		fileInfo.setCreateTime(new Timestamp(new Date().getTime()));
		fileUploadInfoMapper.updateByPrimaryKey(fileInfo);

		// 记录file_upload_record
		FileUploadRecord record = new FileUploadRecord();
		record.setFileId(fileid);
		record.setFileName(fileUploadInfo.getFileName());
		record.setFileServer(fileUploadInfo.getFileServer());
		record.setFilePath(fileid.substring(1, 9));
		record.setOptrType(OPTR_TRANS);
		record.setUserId(userid);
		record.setOptrDate(new Timestamp(new Date().getTime()));
		fileUploadRecordMapper.insert(record);
	}

	@Override
	public Map<String, String> getHttpUrls(List<String> fileids, FileServerConfig fileServerConfig) throws Exception {
		if (fileids == null || fileids.isEmpty()) {
			return null;
		}

		FileUploadInfoExample ex = new FileUploadInfoExample();
		FileUploadInfoExample.Criteria criteria = ex.createCriteria();
		criteria.andFileIdIn(fileids);
		List<FileUploadInfo> fileList = fileUploadInfoMapper.selectByExample(ex);
		if (fileList == null || fileList.isEmpty()) {
			return null;
		} else {
			Map<String, String> result = new HashMap<String, String>();
			for (FileUploadInfo item : fileList) {
				result.put(item.getFileId(), getHttpUrlByFileObject(item, fileServerConfig));
			}
			return result;
		}
	}

	private String getHttpUrlByFileObject(FileUploadInfo fileUploadInfo, FileServerConfig fileServerConfig) {
		try {
			StringBuffer httpurl = new StringBuffer(100);
			if (fileServerConfig == null) {
				fileServerConfig = getServerConfig();
			}

//			if ("U".equals(fileUploadInfo.getFileId().substring(0, 1))) {

				httpurl.append("http://").append(fileServerConfig.getHttpUrl()).append(":").append(fileServerConfig.getHttpPort()).append(fileServerConfig.getHttpPath());
//			}
			httpurl.append(fileUploadInfo.getFilePath()).append("/").append(fileUploadInfo.getFileId()).append(".").append(fileUploadInfo.getFileExt());
			return httpurl.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成符合apache common vfs2标准的uri 根据需要先实现ftp、http和file
	 * 
	 * @param uri
	 * @return
	 */
	private String genVFSURI(String fileName, String extName, String path, FileServerConfig fileServerConfig) throws Exception {
		StringBuffer uri = new StringBuffer(50);
		if (fileServerConfig == null) {
			fileServerConfig = getServerConfig();
		}
		String protocol = fileServerConfig.getProtocol();
		String root = fileServerConfig.getRootPath();
		if ("file".equalsIgnoreCase(protocol)) {
//			File.separator
			uri.append(protocol).append("://").append(root).append("/").append(path).append("/").append(fileName);
		} else {
			String ip = fileServerConfig.getServerIp();
			int port = fileServerConfig.getServerPort();
			String user = fileServerConfig.getUserName();
			String passwd = fileServerConfig.getPassword();
			// DESEncrypt.decode(fileServerConfig.getPassword());

			uri.append(protocol).append("://").append(user).append(":")
					.append(passwd).append("@").append(ip).append(":")
					.append(port).append(root).append("/").append(path)
					.append("/").append(fileName);

		}
		if (!"".equals(extName)) {
			uri.append(".").append(extName);
		}

		return uri.toString();
//		return URLEncoder.encode(uri.toString(),"utf-8");
	}

	/**
	 * 生产FileId,长度为20； U/R+17位时间戳+2位序列
	 * 
	 * @param needValidate
	 *            是否需要权限验证
	 * @return
	 */
	private String genFileId(boolean needValidate) {
		StringBuffer fileid = new StringBuffer(20);

		if (needValidate) {
			fileid.append("R");
		} else {
			fileid.append("U");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		fileid.append(formatter.format(new Date()));

		/*
		 * 为了防止在一毫秒内有并发，还需要带上两位序列号
		 */
		double idx = ad.getAndAdd(1);

		// SequenceUtil.getNextSequence("SEQ_FILEID");
		idx = idx % 99;
		DecimalFormat df = new DecimalFormat("00");
		fileid.append(df.format(idx));
		return fileid.toString();
	}

	@Override
	public String downloadAndUploadFile(FileServerConfig fileServerConfig, String fileid, String extName, String path, InputStream is, String fileName) throws Exception {
		long fileSize = 0;
		// 根据规则生成uri
		FileObject vfsFile = null;
		OutputStream out = null;
		FileSystemManager fsManager = VFS.getManager();

		try {
			String uri = genVFSURI(fileName, extName, path, fileServerConfig);

			if ("sftp".equals(fileServerConfig.getProtocol().toLowerCase())) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);

				vfsFile = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				vfsFile = fsManager.resolveFile(uri);
			}

			if (!vfsFile.exists()) {
				vfsFile.createFile();
			}
			byte content[] = new byte[1024];
			int len = 0;
			out = vfsFile.getContent().getOutputStream();
			while ((len = is.read(content)) != -1) {
				fileSize += len;
				out.write(content, 0, len);
			}
			out.flush();
		} catch (FileSystemException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {

			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {

			}
			try {
				if (vfsFile != null)
					vfsFile.close();

			} catch (Exception e) {

			}
		}
		return fileName;
	}

	@Override
	public void downloadByUri(String remoteUri, String localUri) throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		FileObject sourceFileObject = null;
		FileObject localFileObject = null;
		FileObject tmp = null;
		try {
			long begin = System.currentTimeMillis();
			log.info("start sftp {}, {}", remoteUri, localUri);
			sourceFileObject = getFileObjectByUri(fsManager, remoteUri);
			localFileObject = getFileObjectByUri(fsManager, localUri);
			tmp = getFileObjectByUri(fsManager, remoteUri + ".tmp");
			if (sourceFileObject.exists()) {
				sourceFileObject.moveTo(tmp);
				//sourceFileObject.delete();
				localFileObject.copyFrom(tmp, Selectors.SELECT_ALL);
				sourceFileObject.delete();
			} else {
				log.info("{} had uploaded {}", remoteUri, localUri);
				throw new Exception(remoteUri + "已经上传或正在上传中...");

			}

			log.info("end sftp use times:{}", (System.currentTimeMillis() - begin));
		} catch (Exception e) {
			log.error("downloadByUri error", e);
			throw e;
		} finally {
			if (sourceFileObject != null)
				fsManager.closeFileSystem(sourceFileObject.getFileSystem());

			if (localFileObject != null)
				fsManager.closeFileSystem(localFileObject.getFileSystem());
			
			if (tmp != null)
				fsManager.closeFileSystem(tmp.getFileSystem());
			//((DefaultFileSystemManager) fsManager).close();

		}
		
	}

	public FileObject getFileObjectByUri(FileSystemManager fsManager, String uri) throws Exception {
		FileObject fo = null;
		try {
			if (uri != null && uri.startsWith("sftp")) {
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, false);
				fo = fsManager.resolveFile(uri, fileSystemOptions);
			} else {
				fo = fsManager.resolveFile(uri);
			}
		} catch (Exception e) {
			throw e;
		} finally {
		
		}
		return fo;
	}

	@Override
	public String getHttpUrl(String fileid) throws Exception {
		FileUploadInfo fileUploadInfo = fileUploadInfoMapper.selectByPrimaryKey(fileid);
		if (fileUploadInfo == null) {
			throw new Exception("文件不存在");
		}
		FileServerConfig fileServerConfig=fileServerConfigMapper.selectByPrimaryKey(fileUploadInfo.getFileServer());
		
		if (fileUploadInfo.getFilePath().startsWith(fileServerConfig.getRootPath())) {
			fileUploadInfo.setFilePath(fileUploadInfo.getFilePath().replace(fileServerConfig.getRootPath() + "/", ""));
		}
		return getHttpUrlByFileObject(fileUploadInfo, fileServerConfig);
	}
}
