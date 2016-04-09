package com.wisdom.common.entity;

/**
 * 系统异常code类
 * Created by fusj on 16/3/20.
 */
public class ResultExceptionCode {

    /**
     * 异常编号
     */
    private String code;

    /**
     * 异常对应的url
     */
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
