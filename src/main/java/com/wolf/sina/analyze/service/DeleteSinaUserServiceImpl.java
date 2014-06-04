package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.DELETE_SINA_USER,
        requestConfigs = {
    @RequestConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id")
},
        responseConfigs = {
    @ResponseConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SINA,
        description = "删除新浪用户")
public class DeleteSinaUserServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String userId = messageContext.getParameter("userId");
        this.sinaLocalService.deleteSinaUser(userId);
        messageContext.setMapData(messageContext.getParameterMap());
        messageContext.success();
    }
}
