package com.wolf.sina.spider.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.ResponseState;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import com.wolf.sina.spider.localservice.SpiderLocalService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.UPDATE_SPIDER_USER_COOKIE,
        responseStates = {
    @ResponseState(state = "SUCCESS", desc = "更新成功")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SPIDER,
        desc = "更新爬虫用户的cookie")
public class UpdateSpiderUserCookieServiceImpl implements Service {

    @InjectLocalService()
    private SpiderLocalService spiderLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        List<SpiderUserEntity> spiderUserEntityList = this.spiderLocalService.inquireSpiderUser();
        long thisTime = System.currentTimeMillis();
        String newCookie;
        Map<String, String> updateMap;
        for (SpiderUserEntity spiderUserEntity : spiderUserEntityList) {
            newCookie = this.spiderLocalService.getCookieByLogin(spiderUserEntity.getUserName(), spiderUserEntity.getPassword());
            updateMap = spiderUserEntity.toMap();
            updateMap.put("cookie", newCookie);
            updateMap.put("lastUpdateTime", Long.toString(thisTime));
            this.spiderLocalService.updateSpiderUser(updateMap);
        }
        messageContext.success();
    }
}
