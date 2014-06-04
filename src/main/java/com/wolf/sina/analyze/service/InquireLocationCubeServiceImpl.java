package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.LocationCubeEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_LOCATION_CUBE,
        responseConfigs = {
    @ResponseConfig(name = "location", typeEnum = TypeEnum.CHAR_10, desc = "地区"),
    @ResponseConfig(name = "num", typeEnum = TypeEnum.LONG, desc = "数量")
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SINA,
        description = "查询sina用户地区统计")
public class InquireLocationCubeServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        long pageIndex = messageContext.getPageIndex();
        long pageSize = messageContext.getPageSize();
        List<LocationCubeEntity> locationCubeEntityList = this.sinaLocalService.inquireLocationCube(pageIndex, pageSize);
        messageContext.setEntityListData(locationCubeEntityList);
        messageContext.success();
    }
}
