package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.OutputConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.SinaUserCubeEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_SINA_USER_CUBE,
        returnParameter = {
    @OutputConfig(name = "time", typeEnum = TypeEnum.CHAR_32, desc = "时间"),
    @OutputConfig(name = "num", typeEnum = TypeEnum.LONG, desc = "数量"),
    @OutputConfig(name = "increment", typeEnum = TypeEnum.LONG, desc = "增量")
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SINA,
        description = "查询sina用户总数统计")
public class InquireSinaUserCubeServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        long pageIndex = messageContext.getPageIndex();
        long pageSize = messageContext.getPageSize();
        List<SinaUserCubeEntity> sinaUserCubeEntityList = this.sinaLocalService.inquireSinaUserCube(pageIndex, pageSize);
        messageContext.setEntityListData(sinaUserCubeEntityList);
        messageContext.success();
    }
}
