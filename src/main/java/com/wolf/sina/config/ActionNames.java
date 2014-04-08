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
    //更新cookie
    public final static String UPDATE_SPIDER_USER_COOKIE = "UPDATE_SPIDER_USER_COOKIE";
}
