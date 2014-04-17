package com.wolf.sina.analyze.localservice;

import com.wolf.framework.local.Local;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.entity.SinaExceptionEntity;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface SinaLocalService extends Local {

    public void insertSinaUser(Map<String, String> insertMap);

    public void deleteSinaUser(String userId);

    public void udpateSinaUser(Map<String, String> updateMap);
    
    public long countSinaUser();

    public SinaUserEntity inquireSinaUserByUserId(String userId);

    public List<SinaUserEntity> inquireSinaUser(long pageIndex, long pageSize);

    public List<SinaUserEntity> inquireSinaUserDESC(long pageIndex, long pageSize);

    public void insertSinaException(String userId, String exception);

    public void deleteSinaException(String userId);

    public SinaExceptionEntity inquireSinaExceptionByUserId(String userId);

    public List<SinaExceptionEntity> inquireSinaException(long pageIndex, long pageSize);

    public List<GenderCubeEntity> inquireGenderCube();

    public List<LocationCubeEntity> inquireLocationCube(long pageIndex, long pageSize);
    
    public List<TagCubeEntity> inquireTagCube(long pageIndex, long pageSize);
    
    public void insertSinaUserCube(Map<String, String> insertMap);
    
    public List<SinaUserCubeEntity> inquireSinaUserCube(long pageIndex, long pageSize);
}
