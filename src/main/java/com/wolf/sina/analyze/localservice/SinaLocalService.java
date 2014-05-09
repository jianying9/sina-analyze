package com.wolf.sina.analyze.localservice;

import com.wolf.framework.local.Local;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.SinaUserInfoEntity;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface SinaLocalService extends Local {
    
    public boolean existSinaUser(String userId);

    public void insertSinaUser(Map<String, String> insertMap);

    public void deleteSinaUser(String userId);
    
    public void udpateSinaUser(Map<String, String> updateMap);
    
    public long countSinaUser();
    
    public SinaUserEntity inquireSinaUserByUserId(String userId);

    public SinaUserInfoEntity inquireSinaUserInfoByUserId(String userId);
    
    public List<String> inquireSinaUserId(long pageIndex, long pageSize);

    public List<SinaUserInfoEntity> inquireSinaUser(long pageIndex, long pageSize);
    
    public List<String> inquireSinaUserIdDESC(long pageIndex, long pageSize);

    public List<SinaUserInfoEntity> inquireSinaUserDESC(long pageIndex, long pageSize);

    public List<GenderCubeEntity> inquireGenderCube();

    public List<LocationCubeEntity> inquireLocationCube(long pageIndex, long pageSize);
    
    public List<TagCubeEntity> inquireTagCube(long pageIndex, long pageSize);
    
    public void insertSinaUserCube(Map<String, String> insertMap);
    
    public List<SinaUserCubeEntity> inquireSinaUserCube(long pageIndex, long pageSize);
    
    public void saveToFile(Map<String, String> entityMap);
    
    public Map<String, String> readFormFile(String userId);
    
    public void deleteFile(String userId);
}
