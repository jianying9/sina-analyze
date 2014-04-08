package com.wolf.sina.spider.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.OutputConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import com.wolf.sina.spider.localservice.SpiderLocalService;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_SPIDER_USER,
        returnParameter = {
    @OutputConfig(name = "userName", typeEnum = TypeEnum.CHAR_32, desc = "爬虫帐号", filterTypes = {}),
    @OutputConfig(name = "password", typeEnum = TypeEnum.CHAR_32, desc = "密码", filterTypes = {}),
    @OutputConfig(name = "cookie", typeEnum = TypeEnum.CHAR_4000, desc = "cookie", filterTypes = {}),
    @OutputConfig(name = "lastUpdateTime", typeEnum = TypeEnum.DATE_TIME, desc = "最后更新时间", filterTypes = {})
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SPIDER,
        description = "查询爬虫用户")
public class InquireSpiderUserServiceImpl implements Service {

    @InjectLocalService()
    private SpiderLocalService spiderLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        List<SpiderUserEntity> spiderUserList = this.spiderLocalService.inquireSpiderUser();
        messageContext.setEntityListData(spiderUserList);
        messageContext.success();
    }
}
