package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class GenderCubeHandlerImpl implements CubeHandler {

    private final REntityDao<GenderCubeEntity> genderCubeEntityDao;

    public GenderCubeHandlerImpl(REntityDao<GenderCubeEntity> genderCubeEntityDao) {
        this.genderCubeEntityDao = genderCubeEntityDao;
    }

    @Override
    public void execute(Map<String, String> updateMap) {
        String gender = updateMap.get("gender");
        if (gender != null && gender.isEmpty() == false) {
            this.genderCubeEntityDao.increase(gender, "num", 1);
        }
    }
}
