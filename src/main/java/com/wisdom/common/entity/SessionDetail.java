package com.wisdom.common.entity;

import java.io.Serializable;

/**
 * 登陆用户Session信息
 * Created by fusj on 16/3/17.
 */
public class SessionDetail implements Serializable {

    /**
     * 用户编号
     */
    private Integer accountId;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 手机号码
     */
    private String phoneNo;

    /**
     * 昵称
     */
    private String name;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
