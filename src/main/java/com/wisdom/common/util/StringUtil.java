package com.wisdom.common.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {

	public static <T> String collectionToString(Collection<T> list, String separator) {
		
		if(list == null || list.isEmpty())
			return "";
		
        StringBuilder sb = new StringBuilder();
        
        Iterator<T> it = list.iterator();
        
        int i = 0;
        
        while(it.hasNext())
        {
        	T s = it.next();
            sb.append(s);
            
            if (i < list.size() - 1) {
                sb.append(separator);
            }
            
            i++;
        }
        
        return sb.toString();
    }

    /**
     * 判断是否是数字，包含整数和小数，小数点开头的不行
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return str.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * 判断object对象是否为空，包括null字符串
     * @param object
     * @return
     */
    public static boolean isNotEmptyObject(Object object) {
        if (object == null || "".equals((object + "").trim())||"null".equals((object + "").trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 生成随机6位验证码
     * @return
     */
    public static String genIdentifyCode() {
        Integer code = (int)((Math.random() * 9 + 1) * 100000);
        return code.toString();
    }

    public static void main(String[] args) {
        System.out.println(genIdentifyCode());
    }
}
