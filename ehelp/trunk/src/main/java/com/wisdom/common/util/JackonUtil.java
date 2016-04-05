package com.wisdom.common.util;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSON工具类
 * Created by fusj on 15/12/21.
 */
public class JackonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 把java对象转换成JSON
     * @param obj
     * @return
     * @throws Exception
     */
    public static String writeEntity2JSON(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将JSON转换成java对象
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T readJson2Entity(String json, Class<T> clazz) throws Exception {
        T result = clazz.newInstance();
        result = objectMapper.readValue(json, clazz);

        return result;
    }
}
