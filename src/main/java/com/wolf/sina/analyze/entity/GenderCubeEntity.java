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
        tableName = TableNames.S_GENDER_CUBE,
        dbIndex = TableNames.S_GENDER_CUBE_INDEX)
public final class GenderCubeEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "性别")
    private String gender;
    //
    @RColumnConfig(desc = "数量")
    private long num;

    public String getGender() {
        return gender;
    }

    public long getNum() {
        return num;
    }

    @Override
    public String getKeyValue() {
        return this.gender;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("gender", this.gender);
        map.put("num", Long.toString(this.num));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.gender = entityMap.get("gender");
        this.num = Long.parseLong(entityMap.get("num"));
    }
}
