package com.wolf.sina.spider.entity;

import com.wolf.framework.dao.Entity;
import com.wolf.framework.dao.annotation.ColumnTypeEnum;
import com.wolf.framework.dao.annotation.RColumnConfig;
import com.wolf.framework.dao.annotation.RDaoConfig;
import com.wolf.sina.config.TableNames;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author aladdin
 */
@RDaoConfig(
        tableName = TableNames.SPIDER_USER)
public final class SpiderUserEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "爬虫帐号")
    private String userName;
    //
    @RColumnConfig(desc = "密码")
    private String password;
    //
    @RColumnConfig(desc = "cookie")
    private String cookie;
    //
    @RColumnConfig(desc = "最后更新时间")
    private long lastUpdateTime;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCookie() {
        return cookie;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String getKeyValue() {
        return this.userName;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(4, 1);
        map.put("userName", this.userName);
        map.put("password", this.password);
        map.put("cookie", this.cookie);
        map.put("lastUpdateTime", Long.toString(this.lastUpdateTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.userName = entityMap.get("userName");
        this.password = entityMap.get("password");
        this.cookie = entityMap.get("cookie");
        this.lastUpdateTime = Long.parseLong(entityMap.get("lastUpdateTime"));
    }
}
