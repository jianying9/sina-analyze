package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.OutputConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.SinaUserInfoEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_SINA_USER,
        returnParameter = {
    @OutputConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id"),
    @OutputConfig(name = "gender", typeEnum = TypeEnum.CHAR_10, desc = "性别"),
    @OutputConfig(name = "nickName", typeEnum = TypeEnum.CHAR_32, desc = "昵称"),
    @OutputConfig(name = "empName", typeEnum = TypeEnum.CHAR_32, desc = "姓名"),
    @OutputConfig(name = "tag", typeEnum = TypeEnum.CHAR_4000, desc = "标签"),
    @OutputConfig(name = "location", typeEnum = TypeEnum.CHAR_10, desc = "地区"),
    @OutputConfig(name = "lastUpdateTime", typeEnum = TypeEnum.DATE_TIME, desc = "最后更新时间")
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SINA,
        description = "新增新浪用户")
public class InquireSinaUserServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        long pageIndex = messageContext.getPageIndex();
        long pageSize = messageContext.getPageSize();
        List<SinaUserInfoEntity> sinaUserEntityList = this.sinaLocalService.inquireSinaUserDESC(pageIndex, pageSize);
        messageContext.setEntityListData(sinaUserEntityList);
        messageContext.success();
    }
}
