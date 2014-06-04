package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.TagCubeEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_TAG_CUBE,
        responseConfigs = {
    @ResponseConfig(name = "tag", typeEnum = TypeEnum.CHAR_32, desc = "标签"),
    @ResponseConfig(name = "num", typeEnum = TypeEnum.LONG, desc = "数量")
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SINA,
        description = "查询sina用户标签统计")
public class InquireTagCubeServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        long pageIndex = messageContext.getPageIndex();
        long pageSize = messageContext.getPageSize();
        List<TagCubeEntity> tagCubeEntityList = this.sinaLocalService.inquireTagCube(pageIndex, pageSize);
        messageContext.setEntityListData(tagCubeEntityList);
        messageContext.success();
    }
}
