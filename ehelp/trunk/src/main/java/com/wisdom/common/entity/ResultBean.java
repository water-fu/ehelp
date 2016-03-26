package com.wisdom.common.entity;

/**
 * AJAX返回结果
 * Created by fusj on 16/2/1.
 */
public class ResultBean {
    /**
     * 处理标志
     */
    private Boolean flag;

    /**
     * 处理描述
     */
    private String msg;

    /**
     * 返回结果
     */
    private Object data;

    public ResultBean(Boolean flag) {
        this.flag = flag;
    }

    public ResultBean(Boolean flag, String msg) {
        this(flag);
        this.msg = msg;
    }

    public ResultBean(Boolean flag, Object data) {
        this(flag);
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
