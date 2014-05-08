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
        tableName = TableNames.S_TAG_CUBE,
        dbIndex = TableNames.S_TAG_CUBE_INDEX)
public final class TagCubeEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "标签")
    private String tag;
    //
    @RColumnConfig(desc = "数量")
    private long num;

    public String getTag() {
        return tag;
    }

    public long getNum() {
        return num;
    }

    @Override
    public String getKeyValue() {
        return this.tag;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("tag", this.tag);
        map.put("num", Long.toString(this.num));
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.tag = entityMap.get("tag");
        this.num = Long.parseLong(entityMap.get("num"));
    }
}
