package com.wolf.sina.analyze.localservice;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.framework.utils.StringUtils;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.SinaUserInfoEntity;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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
    private String sinaUserInfoFilePath;

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
        //获取文件存储路径
        String rootFilePath = ApplicationContext.CONTEXT.getParameter("file.path");
        if (rootFilePath == null) {
            rootFilePath = "/data";
        }
        this.sinaUserInfoFilePath = rootFilePath.concat("/sina-user-info/");
        //检测文件夹是否存在
        File file = new File(this.sinaUserInfoFilePath);
        if (file.exists() == false) {
            file.mkdir();
        }
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
        this.deleteFile(userId);
    }

    @Override
    public void udpateSinaUser(Map<String, String> updateMap) {
        this.sinaUserEntityDao.setKeySorce(updateMap, System.currentTimeMillis());
        this.sinaUserEntityDao.update(updateMap);
        for (CubeHandler cubeHandler : this.updateCubeHandlerList) {
            cubeHandler.execute(updateMap);
        }
        //保存到文件
        this.saveToFile(updateMap);
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
        Map<String, String> entityMap = this.readFormFile(userId);
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

    @Override
    public void saveToFile(Map<String, String> entityMap) {
        String userId = entityMap.get("userId");
        if (userId != null) {
            String userFilePath = this.sinaUserInfoFilePath.concat(userId);
            File file = new File(userFilePath);
            if (file.exists()) {
                file.delete();
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(entityMap);
                FileUtils.writeStringToFile(file, json);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public Map<String, String> readFormFile(String userId) {
        Map<String, String> resultMap = null;
        String userFilePath = this.sinaUserInfoFilePath.concat(userId);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            String json = FileUtils.readFileToString(new File(userFilePath));
            rootNode = mapper.readValue(json, JsonNode.class);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        if (rootNode != null) {
            //读数据
            Entry<String, JsonNode> entry;
            String name;
            String value;
            resultMap = new HashMap<String, String>(rootNode.size(), 1);
            Iterator<Entry<String, JsonNode>> iterator = rootNode.getFields();
            while (iterator.hasNext()) {
                entry = iterator.next();
                name = entry.getKey();
                value = entry.getValue().getTextValue();
                value = StringUtils.trim(value);
                resultMap.put(name, value);
            }
        }
        return resultMap;
    }

    @Override
    public void deleteFile(String userId) {
        String userFilePath = this.sinaUserInfoFilePath.concat(userId);
        File file = new File(userFilePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
