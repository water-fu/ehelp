package com.wisdom.weChat.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient的get和post方法
 * Created by fusj on 15/12/21.
 */
public class HttpClientUtil {
    /**
     * get请求
     * @param url
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doGetStr(String url) throws ParseException, IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;

        HttpResponse httpResponse = httpclient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        if(entity != null){
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        return jsonObject;
    }

    /**
     * POST请求
     * @param url
     * @param outStr
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpost = new HttpPost(url);
        JSONObject jsonObject = null;

        httpost.setEntity(new StringEntity(outStr,"UTF-8"));
        HttpResponse response = httpclient.execute(httpost);

        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        jsonObject = JSONObject.fromObject(result);
        return jsonObject;
    }
}
