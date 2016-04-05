package com.wisdom.common.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by fusj on 16/3/10.
 */
public class DateUtil {

    /**
     * 获取当前日期
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 获取当前日期后多少分钟
     * @return
     */
    public static Timestamp getTimestampByMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);

        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 判断过期时间是否有效
     * @param validTime
     * @return true 有效
     */
    public static Boolean isValid(Timestamp validTime) {
        Timestamp now = getTimestamp();

        // 失效
        if(now.after(validTime)) {
            return false;
        }

        return true;
    }
}
