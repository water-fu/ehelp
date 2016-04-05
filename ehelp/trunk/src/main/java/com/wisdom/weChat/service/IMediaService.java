package com.wisdom.weChat.service;

/**
 * 多媒体文件接口
 * Created by fusj on 16/3/31.
 */
public interface IMediaService {

    /**
     * 多媒体下载
     * @param headImg
     * @return
     */
    byte[] getMediaFile(String headImg);
}
