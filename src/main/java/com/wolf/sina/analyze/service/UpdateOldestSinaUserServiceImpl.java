package com.wolf.sina.analyze.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import com.wolf.sina.spider.localservice.InfoEntity;
import com.wolf.sina.spider.localservice.SpiderLocalService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.UPDATE_OLDEST_SINA_USER,
        validateSession = false,
        response = true,
        group = ActionGroupNames.SPIDER,
        description = "更新最早的sina用户信息")
public class UpdateOldestSinaUserServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;
    //
    @InjectLocalService()
    private SpiderLocalService spiderLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        List<SinaUserEntity> sinaUserEntityList = this.sinaLocalService.inquireSinaUser(1, 10);
        InfoEntity infoEntity;
        Map<String, String> updateMap;
        String userId;
        for (SinaUserEntity sinaUserEntity : sinaUserEntityList) {
            userId = sinaUserEntity.getUserId();
            updateMap = new HashMap<String, String>(8, 1);
            updateMap.put("userId", userId);
            //获取详细信息
            infoEntity = this.spiderLocalService.getInfo(userId);
            if (infoEntity != null) {
                updateMap.put("nickName", infoEntity.getNickName());
                updateMap.put("gender", infoEntity.getGender());
                updateMap.put("empName", infoEntity.getEmpName());
                updateMap.put("location", infoEntity.getLocation());
                updateMap.put("tag", infoEntity.getTag());
            }
            this.sinaLocalService.udpateSinaUser(updateMap);
            //获取粉丝
            List<String> followList = this.spiderLocalService.getFollow(sinaUserEntity.getUserId());
            if (followList.isEmpty() == false) {
                Map<String, String> insertMap = new HashMap<String, String>(8, 1);
                SinaUserEntity entity;
                insertMap.put("gender", "");
                insertMap.put("nickName", "");
                insertMap.put("empName", "");
                insertMap.put("location", "");
                insertMap.put("tag", "");
                insertMap.put("lastUpdateTime", "0");
                for (String uid : followList) {
                    entity = this.sinaLocalService.inquireSinaUserByUserId(uid);
                    if(entity == null) {
                        insertMap.put("userId", uid);
                        this.sinaLocalService.insertSinaUser(insertMap);
                    }
                }
            }
        }
        messageContext.success();
    }
}
