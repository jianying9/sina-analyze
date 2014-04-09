package com.wolf.sina.analyze.entity;

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
        tableName = TableNames.SINA_USER)
public final class SinaUserEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "sina用户id")
    private String userId;
    //
    @RColumnConfig(desc = "性别")
    private String gender;
    //
    @RColumnConfig(desc = "昵称")
    private String nickName;
    //
    @RColumnConfig(desc = "姓名")
    private String empName;
    //
    @RColumnConfig(desc = "地区")
    private String location;
    //
    @RColumnConfig(desc = "标签")
    private String tag;
    //
    @RColumnConfig(desc = "最后更新时间")
    private long lastUpdateTime;

    public String getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmpName() {
        return empName;
    }

    public String getLocation() {
        return location;
    }

    public String getTag() {
        return tag;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String getKeyValue() {
        return this.userId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(8, 1);
        map.put("userId", this.userId);
        map.put("gender", this.gender);
        map.put("nickName", this.nickName);
        map.put("empName", this.empName);
        map.put("location", this.location);
        map.put("tag", this.tag);
        map.put("lastUpdateTime", Long.toString(this.lastUpdateTime));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.userId = entityMap.get("userId");
        this.gender = entityMap.get("gender");
        this.nickName = entityMap.get("nickName");
        this.empName = entityMap.get("empName");
        this.location = entityMap.get("location");
        this.tag = entityMap.get("tag");
        this.lastUpdateTime = Long.parseLong(entityMap.get("lastUpdateTime"));
    }
}
