package com.wolf.sina.analyze.service;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.service.Service;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.parameter.InputConfig;
import com.wolf.framework.service.parameter.OutputConfig;
import com.wolf.framework.task.InjectTaskExecutor;
import com.wolf.framework.task.Task;
import com.wolf.framework.task.TaskExecutor;
import com.wolf.framework.worker.context.MessageContext;
import com.wolf.sina.analyze.entity.SinaUserEntity;
import com.wolf.sina.analyze.localservice.SinaLocalService;
import com.wolf.sina.config.ActionGroupNames;
import com.wolf.sina.config.ActionNames;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import com.wolf.sina.spider.localservice.InfoEntity;
import com.wolf.sina.spider.localservice.SpiderLocalService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aladdin
 */
@ServiceConfig(
        actionName = ActionNames.UPDATE_OLDEST_SINA_USER,
        importantParameter = {
    @InputConfig(name = "operate", typeEnum = TypeEnum.CHAR_32, desc = "操作:stop,test,run,check,init")
},
        returnParameter = {
    @OutputConfig(name = "operate", typeEnum = TypeEnum.CHAR_32, desc = "操作:stop,test,run,check,init")
},
        validateSession = false,
        response = true,
        group = ActionGroupNames.SPIDER,
        description = "更新最早的sina用户信息")
public class UpdateOldestSinaUserServiceImpl implements Service {

    private volatile String operate = "stop";
    //
    @InjectLocalService()
    private SinaLocalService sinaLocalService;
    //
    @InjectLocalService()
    private SpiderLocalService spiderLocalService;
    //
    //
    @InjectTaskExecutor
    private TaskExecutor taskExecutor;

    private void updateSinaUser(String userId) {
        Map<String, String> updateMap = new HashMap<String, String>(8, 1);
        updateMap.put("userId", userId);
        //获取详细信息
        InfoEntity infoEntity = this.spiderLocalService.getInfo(userId);
        if (infoEntity != null) {
            updateMap.put("nickName", infoEntity.getNickName());
            updateMap.put("gender", infoEntity.getGender());
            updateMap.put("empName", infoEntity.getEmpName());
            updateMap.put("location", infoEntity.getLocation());
            updateMap.put("tag", infoEntity.getTag());
        }
        this.sinaLocalService.udpateSinaUser(updateMap);
        //获取粉丝
        List<String> followList = this.spiderLocalService.getFollow(userId);
        if (followList.isEmpty() == false) {
            Map<String, String> insertMap = new HashMap<String, String>(8, 1);
            SinaUserEntity entity;
            insertMap.put("gender", "");
            insertMap.put("nickName", "");
            insertMap.put("empName", "");
            insertMap.put("location", "");
            insertMap.put("tag", "");
            insertMap.put("lastUpdateTime", "0");
            for (String uid : followList) {
                entity = this.sinaLocalService.inquireSinaUserByUserId(uid);
                if (entity == null) {
                    insertMap.put("userId", uid);
                    this.sinaLocalService.insertSinaUser(insertMap);
                }
            }
        }
    }

    private void spiderRun() {
        this.operate = "run";
        Task task;
        long pageIndex;
        for (int index = 0; index < 4; index++) {
            pageIndex = index * 20 + 1;
            task = new RunTaskImpl(pageIndex);
            this.taskExecutor.submit(task);
        }
    }

    @Override
    public void execute(MessageContext messageContext) {
        String op = messageContext.getParameter("operate");
        if (op.equals("test")) {
            //测试
            System.out.println("测试爬虫任务");
            List<SinaUserEntity> sinaUserEntityList = this.sinaLocalService.inquireSinaUser(1, 10);
            for (SinaUserEntity sinaUserEntity : sinaUserEntityList) {
                this.updateSinaUser(sinaUserEntity.getUserId());
            }
        } else if (op.equals("run")) {
            //执行
            this.spiderRun();
        } else if (op.equals("stop")) {
            //停止
            System.out.println("停止爬虫任务......");
            this.operate = op;
        } else if (op.equals("init")) {
            this.operate = op;
            Task task = new InitTaskImpl();
            this.taskExecutor.submit(task);
        } else if (op.equals("check")) {
            if (this.operate.equals("stop")) {
                this.spiderRun();
            }
        }
        Map<String, String> resultMap = new HashMap<String, String>(2, 1);
        resultMap.put("operate", this.operate);
        messageContext.setMapData(resultMap);
        messageContext.success();
    }

    /**
     * 执行任务
     */
    private class RunTaskImpl implements Task {

        private final long pageIndex;

        public RunTaskImpl(long pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        public void doWhenRejected() {
            operate = "stop";
        }

        @Override
        public void run() {
            String userId;
            System.out.println("开始执行爬虫任务......");
            while (operate.equals("run")) {
                List<SinaUserEntity> sinaUserEntityList = sinaLocalService.inquireSinaUser(this.pageIndex, 1);
                if (sinaUserEntityList.isEmpty() == false) {
                    userId = sinaUserEntityList.get(0).getUserId();
                    updateSinaUser(userId);
                }
            }
        }
    }

    private class InitTaskImpl implements Task {

        @Override
        public void doWhenRejected() {
            operate = "stop";
        }

        @Override
        public void run() {
            System.out.println("开始初始化爬虫信息...");
            //刷新cookie
            System.out.println("刷新爬虫帐号的cookie...");
            List<SpiderUserEntity> spiderUserEntityList = spiderLocalService.inquireSpiderUser();
            long thisTime = System.currentTimeMillis();
            String newCookie = "";
            Map<String, String> updateMap;
            int times;
            for (SpiderUserEntity spiderUserEntity : spiderUserEntityList) {
                times = 0;
                while (times < 5 && newCookie.isEmpty()) {
                    System.out.println("获取cookie次数:" + times);
                    newCookie = spiderLocalService.getCookieByLogin(spiderUserEntity.getUserName(), spiderUserEntity.getPassword());
                    times++;
                }
                if (newCookie.isEmpty() == false) {
                    updateMap = spiderUserEntity.toMap();
                    updateMap.put("cookie", newCookie);
                    updateMap.put("lastUpdateTime", Long.toString(thisTime));
                    spiderLocalService.updateSpiderUser(updateMap);
                }
            }
            //重建http client
            System.out.println("重建爬虫...");
            spiderLocalService.rebuildHttpClientManager();
            //结束
            operate = "stop";
        }
    }
}
