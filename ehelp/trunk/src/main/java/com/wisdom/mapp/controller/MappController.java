package com.wisdom.mapp.controller;

import com.google.common.collect.Sets;
import com.wisdom.mapp.core.MappContext;
import com.wisdom.mapp.core.client.JsonClientHandler;
import com.wisdom.mapp.model.AppDatapackage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 手机app接口
 * Created by fusj on 16/1/15.
 */
@Controller
public class MappController {

    public final Logger log = LoggerFactory.getLogger(MappController.class);
    public static final Set<String> sessionKeys = Sets.newConcurrentHashSet();

    static {
        sessionKeys.add(MappContext.MAPPCONTEXT_USER);
        sessionKeys.add(MappContext.MAPPCONTEXT_RIGHT);
        sessionKeys.add(MappContext.MAPPCONTEXT_IMEI);
        sessionKeys.add(MappContext.MAPPCONTEXT_DEVICE);
    }

    @Autowired
    private JsonClientHandler<AppDatapackage> jsonHandler;

    @RequestMapping(value = {"/rest"}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getJsonRsp(String msg, HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(msg)) {
            return "msg isBlank";
        }

        /**  **/
        Set<String> rights = new HashSet<String>(0);

        Map<String, Object> attrMap = new HashMap<String, Object>(0);
        attrMap.put(MappContext.MAPPCONTEXT_REQUEST_IP, request.getRemoteHost());
        attrMap.put(MappContext.MAPPCONTEXT_RIGHT, rights);
        attrMap.put(MappContext.MAPPCONTEXT_SESSIONID, request.getSession().getId());
        attrMap.put(MappContext.MAPPCONTEXT_USER, request.getSession().getAttribute(MappContext.MAPPCONTEXT_USER));
        attrMap.put(MappContext.MAPPCONTEXT_IMEI, request.getSession().getAttribute(MappContext.MAPPCONTEXT_IMEI));
        attrMap.put(MappContext.MAPPCONTEXT_DEVICE, request.getSession().getAttribute(MappContext.MAPPCONTEXT_DEVICE));

        attrMap.put("ServletContext", request.getSession().getServletContext());
        StringBuffer url = request.getRequestURL();
        attrMap.put("url", url.substring(0, url.indexOf("/rest/")));

        MappContext.clearContext();

        try {

            String ret = jsonHandler.doHandle(msg, attrMap);

            log.info("ret:" + ret);

            updateSession(request);

            return ret;
        } catch (Exception e) {
            log.error("mapp access error", e);
        }
        return null;
    }

    @RequestMapping(value = {"/updateApk"}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUpdateJsonRsp(HttpServletRequest request) throws Exception {
//        try {
//            XY0024Response response = new XY0024Response();
//            Map map=new HashMap();
//            map.put("pCode","versionCode");
//            List<SysDataDictionary> list=service.dictionarySelect(map);
//            if (list.size()>=0){
//                String des=list.get(0).getDescription();
//                String[] meg=des.split(";");
//                if (meg.length==2){
//                    response.setUpdateMessage(meg[0]);
//                    response.setUrl(meg[1]);
//                }
//                String name=list.get(0).getDicName();
//                String[] names= name.split("_");
//                if(names.length==3){
//                    response.setVersionCode(Integer.valueOf(names[1]));
//                    if ("1".equals(names[2])){
//                        response.setForceUpdate(true);
//                    }
//                }
//            }
//            String ret = JSON.toJSONString(response);
//
//            log.info("ret:" + ret);
//            return ret;
//        } catch (Exception e) {
//            log.error("mapp access error", e);
//        }
        return null;
    }

    private void updateSession(HttpServletRequest request) {
        for (String key : sessionKeys) {
            if (MappContext.getAttribute(key) != null) {
//                if (MappContext.getAttribute(key) instanceof IUserInfo) {
//                    if (log.isInfoEnabled()) {
//                        log.info(((IUserInfo) MappContext.getAttribute(key)).getUserName() + "======" + ((IUserInfo) MappContext.getAttribute(key)).getUserName());
//                    }
//                }
                if (log.isInfoEnabled()) {
                    log.info("{},,,{}", key, MappContext.getAttribute(key));
                }
                request.getSession().setAttribute(key, MappContext.getAttribute(key));
            }
        }
    }

    public String getServerIp() {
        String SERVER_IP = "";
        try {

            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return SERVER_IP;
    }

    public static String getLocalIP(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }
}
