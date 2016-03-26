package com.wisdom.weChat.service.impl;

import com.wisdom.common.cache.SessionCache;
import com.wisdom.common.constants.CommonConstant;
import com.wisdom.weChat.annotation.Token;
import com.wisdom.weChat.constant.UrlConstant;
import com.wisdom.weChat.entity.AccessToken;
import com.wisdom.weChat.entity.JsapiTicket;
import com.wisdom.weChat.service.IJsapiTicketService;
import com.wisdom.weChat.util.HttpClientUtil;
import com.wisdom.weChat.util.WeChatUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * jsapi_ticket
 * Created by fusj on 16/3/17.
 */
@Service
public class JsapiTicketServiceImpl implements IJsapiTicketService {

    private static final Logger logger = LoggerFactory.getLogger(JsapiTicketServiceImpl.class);

    @Autowired
    private SessionCache sessionCache;

    /**
     * jsapi_ticket获取
     * @return
     */
    @Override
    public JsapiTicket getJsapiTicket() throws Exception {
        JsapiTicket jsapiTicket = new JsapiTicket();

        AccessToken accessToken = (AccessToken) sessionCache.get(CommonConstant.ACCESS_TOKEN_VALUE);

        String url = UrlConstant.JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken.getToken());
        JSONObject jsonObject = HttpClientUtil.doGetStr(url);

        if(jsonObject != null) {
            jsapiTicket.setTicket(jsonObject.getString("ticket"));
            jsapiTicket.setExpiresIn(jsonObject.getInt("expires_in"));
        }

        return jsapiTicket;
    }

    /**
     * 根据URL获取签名
     * @param url
     * @return
     */
    @Override
    @Token
    public Map<String, String> sign(String url) throws Exception {
        JsapiTicket jsapiTicket = (JsapiTicket) sessionCache.get(CommonConstant.JSAPI_TICKET_VALUE);

        Map<String, String> ret = new HashMap<>();

        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        StringBuffer sb = new StringBuffer("");
        sb.append("jsapi_ticket=").append(jsapiTicket.getTicket())
                .append("&noncestr=").append(nonce_str)
                .append("&timestamp=").append(timestamp)
                .append("&url=").append(url);

        logger.info("sign-url:" + url);

        // 用sha1加密
        signature = WeChatUtil.getSha1(sb.toString());

        ret.put("url", url);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
