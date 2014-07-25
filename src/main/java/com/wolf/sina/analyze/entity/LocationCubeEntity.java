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
        tableName = TableNames.S_LOCATION_CUBE)
public final class LocationCubeEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "地区")
    private String location;
    //
    @RColumnConfig(desc = "数量")
    private long num;

    public String getLocation() {
        return location;
    }

    public long getNum() {
        return num;
    }

    @Override
    public String getKeyValue() {
        return this.location;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("location", this.location);
        map.put("num", Long.toString(this.num));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.location = entityMap.get("location");
        this.num = Long.parseLong(entityMap.get("num"));
    }
}
