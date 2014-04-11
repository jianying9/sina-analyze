package com.wolf.sina.spider.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.InputConfig;
import com.wolf.framework.service.parameter.OutputConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import com.wolf.sina.spider.localservice.SpiderLocalService;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.DELETE_SPIDER_USER,
        importantParameter = {
    @InputConfig(name = "userName", typeEnum = TypeEnum.CHAR_32, desc = "爬虫帐号")
}, returnParameter = {
    @OutputConfig(name = "userName", typeEnum = TypeEnum.CHAR_32, desc = "爬虫帐号", filterTypes={})
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SPIDER,
        description = "爬虫用户")
public class DeleteSpiderUserServiceImpl implements Service {

    @InjectLocalService()
    private SpiderLocalService spiderLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String userName = messageContext.getParameter("userName");
        this.spiderLocalService.deleteSpiderUser(userName);
        messageContext.setMapData(messageContext.getParameterMap());
        messageContext.success();
    }
}
