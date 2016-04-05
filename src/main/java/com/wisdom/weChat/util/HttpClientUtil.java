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
import sun.applet.Main;

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

    /**
     * 下载
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpResponse doDownStr(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        HttpResponse httpResponse = httpclient.execute(httpGet);

        return httpResponse;
    }

//    public static void main(String[] args) throws Exception {
//        // 拼接请求地址
//        String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
//
//        String accessToken = "KEl9nXW3cztflQkrbNXsYeG8BC2PthJqucCXFULds3tK-yRACnRiug20_utFdy1CUdpqY1VxDGRaxXwx8k9rKn75awqBBeNad1-3v-IAe82D-9XGDMizOEjUfhRsz3h5MZUeAGAWJE";
//        String mediaId = "eo-KYJgrBUNymtpOXNEoNR280W_zUAc2Dw2t9-Y8EgTReleD3WvxR3xfHCIvo9jZ";
//        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//
//        HttpGet httpGet = new HttpGet(requestUrl);
//
//
//        HttpResponse httpResponse = httpclient.execute(httpGet);
//
//        byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
//
//        System.out.println(result);
//        System.out.println(httpResponse);
//    }
}
