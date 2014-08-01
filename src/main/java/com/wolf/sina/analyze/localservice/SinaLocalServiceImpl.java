package com.wolf.sina.analyze.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.framework.utils.SecurityUtils;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.SinaUserExceptionEntity;
import com.wolf.sina.analyze.entity.SinaUserInfoEntity;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

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
    @InjectRDao(clazz = SinaUserExceptionEntity.class)
    private REntityDao<SinaUserExceptionEntity> sinaUserExceptionEntityDao;
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
    //
    private final String hTableName = "SinaUserInfo";
    private HTablePool hTablePool;

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
        //init hbase
        Configuration config = HBaseConfiguration.create();
        this.hTablePool = new HTablePool(config, 10);
    }

    @Override
    public boolean existSinaUser(String userId) {
        return this.sinaUserEntityDao.exist(userId);
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
        this.deleteSinaUserInfo(userId);
    }

    @Override
    public void udpateSinaUser(Map<String, String> updateMap) {
        this.sinaUserEntityDao.setKeySorce(updateMap, System.currentTimeMillis());
        this.sinaUserEntityDao.update(updateMap);
        for (CubeHandler cubeHandler : this.updateCubeHandlerList) {
            cubeHandler.execute(updateMap);
        }
        //保存到文件
        this.saveSinaUserInfo(updateMap);
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
    public SinaUserInfoEntity inquireSinaUserInfoByUserId(String userId) {
        SinaUserInfoEntity sinaUserInfoEntity = null;
        Map<String, String> entityMap = this.getSinaUserInfo(userId);
        if (entityMap != null) {
            sinaUserInfoEntity = new SinaUserInfoEntity();
            sinaUserInfoEntity.parseMap(entityMap);
        }
        return sinaUserInfoEntity;
    }

    @Override
    public List<String> inquireSinaUserId(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaUserEntityDao.inquireKeys(inquirePageContext);
    }

    @Override
    public List<SinaUserInfoEntity> inquireSinaUser(long pageIndex, long pageSize) {
        List<String> userIdList = this.inquireSinaUserId(pageIndex, pageSize);
        List<SinaUserInfoEntity> userEntityList = new ArrayList<SinaUserInfoEntity>(userIdList.size());
        SinaUserInfoEntity sinaUserInfoEntity;
        for (String userId : userIdList) {
            sinaUserInfoEntity = this.inquireSinaUserInfoByUserId(userId);
            if (sinaUserInfoEntity != null) {
                userEntityList.add(sinaUserInfoEntity);
            }
        }
        return userEntityList;
    }

    @Override
    public List<String> inquireSinaUserIdDESC(long pageIndex, long pageSize) {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(pageSize);
        inquirePageContext.setPageIndex(pageIndex);
        return this.sinaUserEntityDao.inquireKeysDESC(inquirePageContext);
    }

    @Override
    public List<SinaUserInfoEntity> inquireSinaUserDESC(long pageIndex, long pageSize) {
        List<String> userIdList = this.inquireSinaUserIdDESC(pageIndex, pageSize);
        List<SinaUserInfoEntity> userEntityList = new ArrayList<SinaUserInfoEntity>(userIdList.size());
        SinaUserInfoEntity sinaUserInfoEntity;
        for (String userId : userIdList) {
            sinaUserInfoEntity = this.inquireSinaUserInfoByUserId(userId);
            if (sinaUserInfoEntity != null) {
                userEntityList.add(sinaUserInfoEntity);
            }
        }
        return userEntityList;
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

    private String createRowKey(String userId) {
        String prefix = SecurityUtils.encryptByMd5(userId);
        prefix = prefix.toLowerCase().substring(0, 4);
        StringBuilder rowKeyBuilder = new StringBuilder(20);
        rowKeyBuilder.append(prefix).append('_').append(userId);
        return rowKeyBuilder.toString();
    }

    @Override
    public void saveSinaUserInfo(Map<String, String> entityMap) {
        String userId = entityMap.get("userId");
        if (userId != null) {
            String[] fileds = {"gender", "nickName", "empName", "location", "tag", "follow", "lastUpdateTime"};
            String fieldValue;
            String rowKey = this.createRowKey(userId);
            Put put = new Put(Bytes.toBytes(rowKey));
            byte[] columnFamily = Bytes.toBytes("INFO");
            for (String filed : fileds) {
                fieldValue = entityMap.get(filed);
                if (fieldValue != null) {
                    put.add(columnFamily, Bytes.toBytes(filed), Bytes.toBytes(fieldValue));
                }
            }
            HTableInterface hTableInterface = this.hTablePool.getTable(this.hTableName);
            try {
                hTableInterface.put(put);
            } catch (IOException ex) {
            } finally {
                try {
                    hTableInterface.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public Map<String, String> getSinaUserInfo(String userId) {
        Map<String, String> resultMap = null;
        String rowKey = this.createRowKey(userId);
        Get get = new Get(Bytes.toBytes(rowKey));
        HTableInterface hTableInterface = this.hTablePool.getTable(this.hTableName);
        try {
            Result result = hTableInterface.get(get);
            if (result != null && result.isEmpty() == false) {
                resultMap = new HashMap<String, String>(result.size() + 1, 1);
                resultMap.put("userId", userId);
                String[] fileds = {"gender", "nickName", "empName", "location", "tag", "follow", "lastUpdateTime"};
                byte[] fieldValue;
                byte[] columnFamily = Bytes.toBytes("INFO");
                for (String field : fileds) {
                    fieldValue = result.getValue(columnFamily, Bytes.toBytes(field));
                    if (fieldValue == null) {
                        resultMap.put(field, "");
                    } else {
                        resultMap.put(field, Bytes.toString(fieldValue));
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            try {
                hTableInterface.close();
            } catch (IOException ex) {
            }
        }
        return resultMap;
    }

    @Override
    public void deleteSinaUserInfo(String userId) {
        String rowKey = this.createRowKey(userId);
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        HTableInterface hTableInterface = this.hTablePool.getTable(this.hTableName);
        try {
            hTableInterface.delete(delete);
        } catch (IOException ex) {
        } finally {
            try {
                hTableInterface.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public boolean existSinaUserException(String userId) {
        return this.sinaUserExceptionEntityDao.exist(userId);
    }

    @Override
    public void insertSinaUserException(String userId) {
        Map<String, String> entityMap = new HashMap<String, String>(2, 1);
        entityMap.put("userId", userId);
        this.sinaUserExceptionEntityDao.insert(entityMap);
    }
}
