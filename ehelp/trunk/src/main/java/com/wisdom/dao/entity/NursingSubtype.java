package com.wisdom.dao.entity;

import java.sql.Timestamp;

public class NursingSubtype {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.PARENT_ID
     *
     * @mbggenerated
     */
    private Integer parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.NAME
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.SIMPLE_PINYIN
     *
     * @mbggenerated
     */
    private String simplePinyin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.PRICE
     *
     * @mbggenerated
     */
    private String price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.CONTENT
     *
     * @mbggenerated
     */
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.IS_DEL
     *
     * @mbggenerated
     */
    private String isDel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_nursing_subtype.CREATE_TIME
     *
     * @mbggenerated
     */
    private Timestamp createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.ID
     *
     * @return the value of t_nursing_subtype.ID
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.ID
     *
     * @param id the value for t_nursing_subtype.ID
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.PARENT_ID
     *
     * @return the value of t_nursing_subtype.PARENT_ID
     *
     * @mbggenerated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.PARENT_ID
     *
     * @param parentId the value for t_nursing_subtype.PARENT_ID
     *
     * @mbggenerated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.NAME
     *
     * @return the value of t_nursing_subtype.NAME
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.NAME
     *
     * @param name the value for t_nursing_subtype.NAME
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.SIMPLE_PINYIN
     *
     * @return the value of t_nursing_subtype.SIMPLE_PINYIN
     *
     * @mbggenerated
     */
    public String getSimplePinyin() {
        return simplePinyin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.SIMPLE_PINYIN
     *
     * @param simplePinyin the value for t_nursing_subtype.SIMPLE_PINYIN
     *
     * @mbggenerated
     */
    public void setSimplePinyin(String simplePinyin) {
        this.simplePinyin = simplePinyin == null ? null : simplePinyin.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.PRICE
     *
     * @return the value of t_nursing_subtype.PRICE
     *
     * @mbggenerated
     */
    public String getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.PRICE
     *
     * @param price the value for t_nursing_subtype.PRICE
     *
     * @mbggenerated
     */
    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.CONTENT
     *
     * @return the value of t_nursing_subtype.CONTENT
     *
     * @mbggenerated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.CONTENT
     *
     * @param content the value for t_nursing_subtype.CONTENT
     *
     * @mbggenerated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.IS_DEL
     *
     * @return the value of t_nursing_subtype.IS_DEL
     *
     * @mbggenerated
     */
    public String getIsDel() {
        return isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.IS_DEL
     *
     * @param isDel the value for t_nursing_subtype.IS_DEL
     *
     * @mbggenerated
     */
    public void setIsDel(String isDel) {
        this.isDel = isDel == null ? null : isDel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_nursing_subtype.CREATE_TIME
     *
     * @return the value of t_nursing_subtype.CREATE_TIME
     *
     * @mbggenerated
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_nursing_subtype.CREATE_TIME
     *
     * @param createTime the value for t_nursing_subtype.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}