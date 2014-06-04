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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.TIMER_SAVE_SINA_USER_NUM,
        requestConfigs = {
    @RequestConfig(name = "time", typeEnum = TypeEnum.CHAR_32, desc = "时间")
},
        responseConfigs = {
    @ResponseConfig(name = "time", typeEnum = TypeEnum.CHAR_32, desc = "时间"),
    @ResponseConfig(name = "num", typeEnum = TypeEnum.CHAR_10, desc = "数量")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SINA,
        description = "保存下当前的sina用户总数量")
public class TimerSaveSinaUserNumServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String time = messageContext.getParameter("time");
        long num = this.sinaLocalService.countSinaUser();
        Map<String, String> insertMap = new HashMap<String, String>(2, 1);
        insertMap.put("time", time);
        insertMap.put("num", Long.toString(num));
        this.sinaLocalService.insertSinaUserCube(insertMap);
        messageContext.setMapData(insertMap);
        messageContext.success();
    }
}
