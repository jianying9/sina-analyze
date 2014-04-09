package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.sina.analyze.entity.SinaExceptionEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
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

    @Override
    public void init() {
    }

    @Override
    public void insertSinaUser(Map<String, String> insertMap) {
        this.sinaUserEntityDao.setKeySorce(insertMap, -1);
        this.sinaUserEntityDao.insert(insertMap);
    }

    @Override
    public void deleteSinaUser(String userId) {
        this.sinaUserEntityDao.delete(userId);
    }

    @Override
    public void udpateSinaUser(Map<String, String> updateMap) {
        this.sinaUserEntityDao.setKeySorce(updateMap, System.currentTimeMillis());
        this.sinaUserEntityDao.update(updateMap);
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
}
