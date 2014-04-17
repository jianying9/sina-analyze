package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.entity.SinaExceptionEntity;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = SinaLocalService.class,
        description = "sina相关接口")
public class SinaLocalServiceImpl implements SinaLocalService {
    
    @InjectRDao(clazz = SinaUserEntity.class)
    private REntityDao<SinaUserEntity> sinaUserEntityDao;
    //
    @InjectRDao(clazz = SinaExceptionEntity.class)
    private REntityDao<SinaExceptionEntity> sinaExceptionEntityDao;
    //
    @InjectRDao(clazz = GenderCubeEntity.class)
    private REntityDao<GenderCubeEntity> genderCubeEntityDao;
    //
    @InjectRDao(clazz = LocationCubeEntity.class)
    private REntityDao<LocationCubeEntity> locationCubeEntityDao;
    //
    @InjectRDao(clazz = TagCubeEntity.class)
    private REntityDao<TagCubeEntity> tagCubeEntityDao;
    //
    @InjectRDao(clazz = SinaUserCubeEntity.class)
    private REntityDao<SinaUserCubeEntity> sinaUserCubeEntityDao;
    //
    private final List<CubeHandler> updateCubeHandlerList = new ArrayList<CubeHandler>();
    private CubeHandler sinaUserCubeHandler;
    
    @Override
    public void init() {
        CubeHandler cubeHandler = new GenderCubeHandlerImpl(this.genderCubeEntityDao);
        this.updateCubeHandlerList.add(cubeHandler);
        //
        cubeHandler = new LocationCubeHandlerImpl(this.locationCubeEntityDao);
        this.updateCubeHandlerList.add(cubeHandler);
        //
        cubeHandler = new TagCubeHandlerImpl(this.tagCubeEntityDao);
        this.updateCubeHandlerList.add(cubeHandler);
        //
        this.sinaUserCubeHandler = new SinaUserCubeHandlerImpl(this.sinaUserCubeEntityDao);
    }
    
    @Override
    public void insertSinaUser(Map<String, String> insertMap) {
        this.sinaUserEntityDao.setKeySorce(insertMap, -1);
        this.sinaUserEntityDao.insert(insertMap);
        this.sinaUserCubeHandler.execute(insertMap);
    }
    
    @Override
    public void deleteSinaUser(String userId) {
        this.sinaUserEntityDao.delete(userId);
    }
    
    @Override
    public void udpateSinaUser(Map<String, String> updateMap) {
        this.sinaUserEntityDao.setKeySorce(updateMap, System.currentTimeMillis());
        this.sinaUserEntityDao.update(updateMap);
        for (CubeHandler cubeHandler : this.updateCubeHandlerList) {
            cubeHandler.execute(updateMap);
        }
    }
    
    @Override
    public long countSinaUser() {
        return this.sinaUserEntityDao.count();
    }
    
    @Override
    public SinaUserEntity inquireSinaUserByUserId(String userId) {
        return this.sinaUserEntityDao.inquireByKey(userId);
    }
    
    @Override
    public List<SinaUserEntity> inquireSinaUser(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaUserEntityDao.inquire(inquirePageContext);
    }
    
    @Override
    public List<SinaUserEntity> inquireSinaUserDESC(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaUserEntityDao.inquireDESC(inquirePageContext);
    }
    
    @Override
    public void insertSinaException(String userId, String exception) {
        Map<String, String> insertMap = new HashMap<String, String>(4, 1);
        insertMap.put("userId", userId);
        insertMap.put("exception", exception);
        insertMap.put("lastUpdateTime", Long.toString(System.currentTimeMillis()));
        this.sinaExceptionEntityDao.insert(insertMap);
    }
    
    @Override
    public void deleteSinaException(String userId) {
        this.sinaExceptionEntityDao.delete(userId);
    }
    
    @Override
    public List<SinaExceptionEntity> inquireSinaException(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaExceptionEntityDao.inquire(inquirePageContext);
    }
    
    @Override
    public SinaExceptionEntity inquireSinaExceptionByUserId(String userId) {
        return this.sinaExceptionEntityDao.inquireByKey(userId);
    }
    
    @Override
    public List<GenderCubeEntity> inquireGenderCube() {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(10);
        return this.genderCubeEntityDao.inquire(inquirePageContext);
    }
    
    @Override
    public List<LocationCubeEntity> inquireLocationCube(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.locationCubeEntityDao.inquireDESC(inquirePageContext);
    }
    
    @Override
    public List<TagCubeEntity> inquireTagCube(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.tagCubeEntityDao.inquireDESC(inquirePageContext);
    }
    
    @Override
    public void insertSinaUserCube(Map<String, String> insertMap) {
        this.sinaUserCubeEntityDao.insert(insertMap);
    }
    
    @Override
    public List<SinaUserCubeEntity> inquireSinaUserCube(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaUserCubeEntityDao.inquireDESC(inquirePageContext);
    }
}
