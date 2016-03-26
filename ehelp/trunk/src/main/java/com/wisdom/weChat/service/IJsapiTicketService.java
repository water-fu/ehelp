package com.wisdom.weChat.service;

import com.wisdom.weChat.entity.JsapiTicket;

import java.util.Map;

/**
 * jsapi_ticket业务处理
 * Created by fusj on 16/3/17.
 */
public interface IJsapiTicketService {
    /**
     * 获取jsapi_ticket
     * @return
     */
    JsapiTicket getJsapiTicket() throws Exception;

    /**
     * 根据URL获取签名
     * @param url
     * @return
     */
    Map<String, String> sign(String url) throws Exception;
}
