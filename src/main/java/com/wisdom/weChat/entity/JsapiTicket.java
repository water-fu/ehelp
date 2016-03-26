package com.wisdom.weChat.entity;

import java.io.Serializable;

/**
 * jsapi_ticket
 * Created by fusj on 16/3/17.
 */
public class JsapiTicket implements Serializable {
    /**
     * 票据
     */
    private String ticket;

    /**
     * 有效时间
     */
    private int expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
