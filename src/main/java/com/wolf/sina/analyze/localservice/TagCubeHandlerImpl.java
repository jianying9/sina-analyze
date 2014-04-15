package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class TagCubeHandlerImpl implements CubeHandler {

    private REntityDao<TagCubeEntity> tagCubeEntityDao;

    public TagCubeHandlerImpl(REntityDao<TagCubeEntity> tagCubeEntityDao) {
        this.tagCubeEntityDao = tagCubeEntityDao;
    }

    @Override
    public void execute(Map<String, String> updateMap) {
        String tags = updateMap.get("tag");
        if (tags != null && tags.isEmpty() == false) {
            String[] tagArr = tags.split(",");
            long sorce;
            for (String tag : tagArr) {
                if (tag.isEmpty() == false) {
                    sorce = this.tagCubeEntityDao.increase(tag, "num", 1);
                    this.tagCubeEntityDao.updateKeySorce(tag, sorce);
                }
            }
        }
    }
}
