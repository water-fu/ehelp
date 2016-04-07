package com.wisdom.web.common.constants;

/**
 * 缓存数据常量
 * Created by fusj on 16/3/9.
 */
public class SysParamDetailConstant {


    /**
     * 账号类型
     */
    public static final String ACCOUNT_TYPE_PATIENT = "1"; // 患者

    public static final String ACCOUNT_TYPE_NURSING = "2"; // 护士

    public static final String ACCOUNT_TYPE_KF = "3"; // 客服

    public static final String ACCOUNT_TYOE_DOCTOR = "4"; // 医生

    public static final String ACCOUNT_TYPE_ADMIN = "888"; // 系统管理员

    public static final String ACCOUNT_TYPE_SYSTEM = "999"; // 后台管理员

    /**
     * 账号状态
     */
    public static final String ACCOUNT_STATUS_NEW = "1"; // 新增

    public static final String ACCOUNT_STATUS_AUTHEN = "2"; // 提交认证

    public static final String ACCOUNT_STATUS_CONFIRM = "3"; // 认证确认

    public static final String ACCOUNT_STATUS_FAILED = "-1"; // 认证失败

    public static final String ACCOUNT_STATUS_INVALID = "0"; // 账号失效

    /**
     * 账号是否分红
     */
    public static final String IS_REWARD_TRUE = "1"; // 分红

    public static final String IS_REWARD_FALSE = "0"; // 不分红

    /**
     * 亲属关系
     */
    public static final String RELATION_MYSELF = "1"; // 自己

    /**
     * 是否使用
     */
    public static final String IS_USED_FALSE = "0"; // 未使用

    public static final String IS_USED_TRUE = "1"; // 已使用

    /**
     * 是否删除
     */
    public static final String IS_DEL_TRUE = "1";  // 是

    public static final String IS_DEL_FALSE = "0"; // 否

    /**
     * 加密方式
     */
    public static final String AES = "AESEncrypt"; // AES加密

    public static final String MD5 = "MD5Encrypt"; // MD5加密

    /**
     * 验证码类型
     */
    public static final String IDENTIFY_TYPE_REGISTER = "1"; // 注册

    public static final String IDENTIFY_TYPE_FOTGET = "2"; // 忘记密码

    public static final String IDENTIFY_TYPE_BIND = "3"; // 微信绑定

    /**
     * 患者状态
     */
    public static final String PATIENT_STATUS_ADD = "1"; // 新增

    /**
     * 登陆类型
     */
    public static final String LOGIN_FROM_SYSTEM = "1"; // 系统登陆

    public static final String LOGIN_FROM_WECHAT = "2"; // 微信登陆

    public static final String LOGIN_FROM_WEIBO = "3"; // 微博登陆

    public static final String LOGIN_FROM_QQ = "4"; // QQ登陆
}
