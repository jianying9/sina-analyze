package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.GenderCubeEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.List;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_GENDER_CUBE,
        responseConfigs = {
    @ResponseConfig(name = "gender", typeEnum = TypeEnum.CHAR_10, desc = "性别"),
    @ResponseConfig(name = "num", typeEnum = TypeEnum.LONG, desc = "数量")
},
        validateSession = false,
        page = true,
        response = true,
        group = ActionGroupNames.SINA,
        description = "查询sina用户性别统计")
public class InquireGenderCubeServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        List<GenderCubeEntity> genderCubeEntityList = this.sinaLocalService.inquireGenderCube();
        messageContext.setEntityListData(genderCubeEntityList);
        messageContext.success();
    }
}
