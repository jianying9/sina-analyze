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
        tableName = TableNames.SINA_USER_CUBE)
public final class SinaUserCubeEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "时间")
    private String time;
    //
    @RColumnConfig(desc = "数量")
    private long num;

    public String getTime() {
        return time;
    }
    
    public long getNum() {
        return num;
    }

    @Override
    public String getKeyValue() {
        return this.time;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("time", this.time);
        map.put("num", Long.toString(this.num));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.time = entityMap.get("time");
        this.num = Long.parseLong(entityMap.get("num"));
    }
}
