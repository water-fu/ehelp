package com.wisdom.dao.mapper.vfs;

import com.wisdom.dao.entity.vfs.FileUploadInfo;
import com.wisdom.dao.entity.vfs.FileUploadInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileUploadInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int countByExample(FileUploadInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int deleteByExample(FileUploadInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String fileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int insert(FileUploadInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int insertSelective(FileUploadInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    List<FileUploadInfo> selectByExample(FileUploadInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    FileUploadInfo selectByPrimaryKey(String fileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") FileUploadInfo record, @Param("example") FileUploadInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") FileUploadInfo record, @Param("example") FileUploadInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(FileUploadInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(FileUploadInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_upload_info
     *
     * @mbggenerated
     */
    void insertBatch(List<FileUploadInfo> recordLst);
}