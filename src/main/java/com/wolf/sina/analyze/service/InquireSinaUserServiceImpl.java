package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.InputConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_SINA_USER,
        importantParameter = {
    @InputConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SPIDER,
        description = "新增新浪用户")
public class InquireSinaUserServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String userId = messageContext.getParameter("userId");
        Map<String, String> insertMap = new HashMap<String, String>(8, 1);
        insertMap.put("userId", userId);
        insertMap.put("gender", "");
        insertMap.put("nickName", "");
        insertMap.put("empName", "");
        insertMap.put("location", "");
        insertMap.put("tag", "");
        insertMap.put("lastUpdateTime", "0");
        this.sinaLocalService.insertSinaUser(insertMap);
        messageContext.success();
    }
}
