package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.RequestConfig;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.entity.SinaUserInfoEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.INQUIRE_SINA_USER_BY_USER_ID,
        requestConfigs = {
    @RequestConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id")
},
        responseConfigs = {
    @ResponseConfig(name = "userId", typeEnum = TypeEnum.CHAR_32, desc = "sina帐号id"),
    @ResponseConfig(name = "gender", typeEnum = TypeEnum.CHAR_10, desc = "性别"),
    @ResponseConfig(name = "nickName", typeEnum = TypeEnum.CHAR_32, desc = "昵称"),
    @ResponseConfig(name = "empName", typeEnum = TypeEnum.CHAR_32, desc = "姓名"),
    @ResponseConfig(name = "tag", typeEnum = TypeEnum.CHAR_4000, desc = "标签"),
    @ResponseConfig(name = "follow", typeEnum = TypeEnum.CHAR_4000, desc = "关注"),
    @ResponseConfig(name = "location", typeEnum = TypeEnum.CHAR_10, desc = "地区"),
    @ResponseConfig(name = "lastUpdateTime", typeEnum = TypeEnum.DATE_TIME, desc = "最后更新时间")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SINA,
        description = "新增新浪用户")
public class InquireSinaUserByUserIdServiceImpl implements Service {

    @InjectLocalService()
    private SinaLocalService sinaLocalService;

    @Override
    public void execute(MessageContext messageContext) {
        String userId = messageContext.getParameter("userId");
        SinaUserEntity sinaUserEntity = this.sinaLocalService.inquireSinaUserByUserId(userId);
        if (sinaUserEntity != null) {
            SinaUserInfoEntity sinaUserInfoEntity = this.sinaLocalService.inquireSinaUserInfoByUserId(userId);
            if (sinaUserInfoEntity != null) {
                messageContext.setEntityData(sinaUserEntity);
            } else {
                Map<String, String> resultMap = sinaUserEntity.toMap();
                resultMap.put("gender", "");
                resultMap.put("nickName", "");
                resultMap.put("empName", "");
                resultMap.put("tag", "");
                resultMap.put("follow", "");
                resultMap.put("location", "");
                messageContext.setMapData(resultMap);
            }
        }
        messageContext.success();
    }
}
