package com.wisdom.web.controller.common;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.entity.ResultBean;
import com.wisdom.common.entity.SessionDetail;
import com.wisdom.common.util.CookieUtil;
import com.wisdom.dao.entity.vfs.FileUploadInfo;
import com.wisdom.service.service.vfs.IFileService;
import com.wisdom.service.service.vfs.IFileUploadInfoService;
import com.wisdom.web.common.BaseController;
import com.wisdom.web.common.constants.CommonConstant;
import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件处理
 * Created by fusj on 16/4/5.
 */
@Controller
@RequestMapping("file")
public class FileController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IFileService fileService;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private IFileUploadInfoService fileUploadInfoService;
    /**
     * 文件上传
     * @return
     */
    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            String fileName = file.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            String fileId = fileService.uploadFile(fileService.getServerConfig(), file.getBytes(), fileName.substring(index + 1),
                    fileName.substring(0, index), sessionDetail.getAccountId(), true, true);

            ResultBean resultBean = new ResultBean(true);
            resultBean.setData(fileId);

            return resultBean;
        } catch (Exception ex) {
            return ajaxException(ex);
        }
    }

    /**
     * 图片下载
     * @param fileId
     * @param request
     * @param response
     */
    @RequestMapping(value = "imageDownload", method = RequestMethod.GET)
    public void imageDownload(String fileId, HttpServletRequest request, HttpServletResponse response) {
        FileObject fileObject = null;
        ServletOutputStream toClient = null;
        try {
            Cookie cookie = CookieUtil.getCookieByName(request, CommonConstant.COOKIE_VALUE);
            SessionDetail sessionDetail = (SessionDetail) sessionCache.get(cookie.getValue());

            FileUploadInfo fileUploadInfo = fileUploadInfoService.selectByPrimaryKey(fileId);
            fileObject =fileService.downloadFile(fileId, fileService.getServerConfig());

            BufferedImage img = ImageIO.read(fileObject.getContent().getInputStream());
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/" + fileUploadInfo.getFileExt());

            // 得到向客户端输出二进制数据的对象
            toClient = response.getOutputStream();
            ImageIO.write(img, fileUploadInfo.getFileExt(), toClient);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if(fileObject != null) {
                try {
                    fileObject.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if(toClient != null) {
                try {
                    toClient.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }
}
