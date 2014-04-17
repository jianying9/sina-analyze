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
    //查询某个sina的用户信息
    public final static String INQUIRE_SINA_USER_BY_USER_ID = "INQUIRE_SINA_USER_BY_USER_ID";
    //查询sina用户性别统计
    public final static String INQUIRE_GENDER_CUBE = "INQUIRE_GENDER_CUBE";
    //查询sina用户地区统计
    public final static String INQUIRE_LOCATION_CUBE = "INQUIRE_LOCATION_CUBE";
    //查询sina用户标签统计
    public final static String INQUIRE_TAG_CUBE = "INQUIRE_TAG_CUBE";
    //timer:每小时保存该时间点用户的总数
    public final static String TIMER_SAVE_SINA_USER_NUM = "TIMER_SAVE_SINA_USER_NUM";
    //查询sina用户数统计
    public final static String INQUIRE_SINA_USER_CUBE = "INQUIRE_SINA_USER_CUBE";
}
