package com.wisdom.web.common.constants;

/**
 * 常用常量
 * Created by fusj on 16/3/9.
 */
public class CommonConstant {

    /**
     * 存入cookie的value
     */
    public static final String COOKIE_VALUE = "ICARE_SESSION_ID";

    /**
     * cookie生命周期，单位秒
     */
    public static final Integer SESSION_TIME_OUT = 30 *  60;

    /**
     * cookie24小时过期
     */
    public static final Integer SESSION_TIME_OUT_DAY = 24 * 60 * 60;

    /**
     * 微信公众号access_token存放到redis的key
     */
    public static final String ACCESS_TOKEN_VALUE = "ACCESS_TOKEN";

    /**
     * 微信公众号jsapi_ticket存放到redis的key
     */
    public static final String JSAPI_TICKET_VALUE = "JSAPI_TICKET";

    /**
     * model返回pageInfo的key
     */
    public static final String PAGE_INFO = "pageInfo";

    /**
     * 密码加密密钥
     */
    public static final String SALT = "iCare";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PWD = "123456";

    /**
     * 验证码过期时间
     */
    public static final Integer IDENTIFY_CODE_INVALID = 15;

    /**
     * 在验证码过期时间内连续发送超时次数
     */
    public static final Integer SEND_IDENTIFY_TIMES = 10;

    /**
     * 查询单个
     */
    public static final String SINGLE_TYPE = "single";

    /**
     * 查询列表
     */
    public static final String LIST_TYPE = "list";

}
