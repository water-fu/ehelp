package com.wisdom.dao.mapper;

import com.wisdom.dao.entity.IdentifyCode;
import com.wisdom.dao.entity.IdentifyCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdentifyCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int countByExample(IdentifyCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int deleteByExample(IdentifyCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int insert(IdentifyCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int insertSelective(IdentifyCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    List<IdentifyCode> selectByExample(IdentifyCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    IdentifyCode selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") IdentifyCode record, @Param("example") IdentifyCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") IdentifyCode record, @Param("example") IdentifyCodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(IdentifyCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(IdentifyCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_identify_code
     *
     * @mbggenerated
     */
    void insertBatch(List<IdentifyCode> recordLst);
}