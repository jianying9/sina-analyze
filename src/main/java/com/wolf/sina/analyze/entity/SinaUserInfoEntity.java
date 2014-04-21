package com.wolf.sina.analyze.entity;

import com.wolf.framework.dao.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author aladdin
 */
public final class SinaUserInfoEntity extends Entity {

    private String userId;
    //
    private String gender;
    //
    private String nickName;
    //
    private String empName;
    //
    private String location;
    //
    private String tag;
    //
    private String follow;
    //
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

    public String getFollow() {
        return follow;
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
        map.put("follow", this.follow);
        map.put("lastUpdateTime", Long.toString(this.lastUpdateTime));
        return map;
    }

    @Override
    public void parseMap(Map<String, String> entityMap) {
        this.userId = entityMap.get("userId");
        this.gender = entityMap.get("gender");
        this.nickName = entityMap.get("nickName");
        this.empName = entityMap.get("empName");
        this.location = entityMap.get("location");
        this.tag = entityMap.get("tag");
        this.follow = entityMap.get("follow");
        this.lastUpdateTime = Long.parseLong(entityMap.get("lastUpdateTime"));
    }
}
