package com.wolf.sina.config;

/**
 *
 * @author aladdin
 */
public class ResponseFlags {

    //邮箱或昵称不存在
    public final static String FAILURE_LOGIN_NOT_EXIST = "FAILURE_LOGIN_NOT_EXIST";
    //用户id不存在
    public final static String FAILURE_USER_ID_NOT_EXIST = "FAILURE_USER_ID_NOT_EXIST";
    //密码错误
    public final static String FAILURE_PASSWORD_ERROR = "FAILURE_PASSWORD_ERROR";
    //邮箱已经被使用
    public final static String FAILURE_USER_EMAIL_USED = "FAILURE_USER_EMAIL_USED";
    //昵称已经被使用
    public final static String FAILURE_USER_NICK_NAME_USED = "FAILURE_USER_NICK_NAME_USED";
    //点数不足
    public final static String FAILURE_USER_POINT_LESS = "FAILURE_USER_POINT_LESS";
}
