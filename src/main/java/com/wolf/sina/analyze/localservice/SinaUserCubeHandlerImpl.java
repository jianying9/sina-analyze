package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.utils.TimeUtils;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class SinaUserCubeHandlerImpl implements CubeHandler {

    private final REntityDao<SinaUserCubeEntity> sinaUserCubeEntityDao;

    public SinaUserCubeHandlerImpl(REntityDao<SinaUserCubeEntity> sinaUserCubeEntityDao) {
        this.sinaUserCubeEntityDao = sinaUserCubeEntityDao;
    }

    @Override
    public void execute(Map<String, String> updateMap) {
        String userId = updateMap.get("userId");
        if (userId != null && userId.isEmpty() == false) {
            String time = TimeUtils.getDateFotmatYYMMDDHHmmSS();
            time = time.substring(0, 13);
            this.sinaUserCubeEntityDao.increase(time, "increment", 1);
        }
    }
}
