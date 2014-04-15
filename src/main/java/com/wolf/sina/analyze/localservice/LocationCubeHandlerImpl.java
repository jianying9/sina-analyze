package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class LocationCubeHandlerImpl implements CubeHandler {

    private REntityDao<LocationCubeEntity> locationCubeEntityDao;

    public LocationCubeHandlerImpl(REntityDao<LocationCubeEntity> locationCubeEntityDao) {
        this.locationCubeEntityDao = locationCubeEntityDao;
    }

    @Override
    public void execute(Map<String, String> updateMap) {
        String location = updateMap.get("location");
        if (location != null && location.isEmpty() == false) {
            int index = location.indexOf(" ");
            if (index > -1) {
                location = location.substring(0, index);
            }
            this.locationCubeEntityDao.increase(location, "num", 1);
        }
    }
}
