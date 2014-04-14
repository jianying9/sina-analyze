package com.wolf.sina.config;

/**
 *
 * @author aladdin
 */
public class ActionNames {
    //-----------------------文件------------------------//
    //保存图片

    public final static String INSERT_IMAGE = "INSERT_IMAGE";
    //根据imageId查询图片数据
    public final static String INQUIRE_IMAGE_BY_KEY = "INQUIRE_IMAGE_BY_KEY";
    //-----------------------spider用户------------------------//
    //新增爬虫帐号
    public final static String INSERT_SPIDER_USER = "INSERT_SPIDER_USER";
    //删除爬虫帐号
    public final static String DELETE_SPIDER_USER = "DELETE_SPIDER_USER";
    //查询爬虫帐号
    public final static String INQUIRE_SPIDER_USER = "INQUIRE_SPIDER_USER";
    //获取爬虫帐号cookie
    public final static String UPDATE_SPIDER_USER_COOKIE = "UPDATE_SPIDER_USER_COOKIE";
    //-------------------------sina---------------------------------//
    //新增sina用户
    public final static String INSERT_SINA_USER = "INSERT_SINA_USER";
    //删除sina用户
    public final static String DELETE_SINA_USER = "DELETE_SINA_USER";
    //更新sina用户信息
    public final static String UPDATE_SINA_USER = "UPDATE_SINA_USER";
    //更新最早的sina用户信息
    public final static String UPDATE_OLDEST_SINA_USER = "UPDATE_OLDEST_SINA_USER";
    //查询sina的异常信息
    public final static String INQUIRE_SINA_EXCEPTION = "INQUIRE_SINA_EXCEPTION";
    //查询sina的用户信息
    public final static String INQUIRE_SINA_USER = "INQUIRE_SINA_USER";
}
