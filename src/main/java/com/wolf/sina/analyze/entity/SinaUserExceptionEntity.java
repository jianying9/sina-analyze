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
        tableName = TableNames.S_SINA_USER_EXCEPTON,
        dbIndex = TableNames.S_SINA_USER_EXCEPTION_INDEX)
public final class SinaUserExceptionEntity extends Entity {

    @RColumnConfig(columnTypeEnum = ColumnTypeEnum.KEY, desc = "sina用户id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    @Override
    public String getKeyValue() {
        return this.userId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(2, 1);
        map.put("userId", this.userId);
        return map;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.userId = entityMap.get("userId");
    }
}
