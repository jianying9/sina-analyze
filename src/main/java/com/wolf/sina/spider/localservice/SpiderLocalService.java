package com.wolf.sina.spider.localservice;

import com.wolf.framework.local.Local;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
public interface SpiderLocalService extends Local {
    
    public void insertSpiderUser(String userName, String password);
    
    public void updateSpiderUser(Map<String, String> updateMap);
    
    public List<SpiderUserEntity> inquireSpiderUser();
    
    public void deleteSpiderUser(String userName);
    
    public String getCookieByLogin(String userName, String password);
}
