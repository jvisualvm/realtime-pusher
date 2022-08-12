package com.risen.realtime.business.job;

import com.risen.common.util.LogUtil;
import com.risen.realtime.business.filter.IvMDBDirectoryFilter;
import com.risen.realtime.business.listener.IvMDBFileListener;
import com.risen.realtime.framework.cache.PushSourceCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.entity.PushSourceEntity;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.service.TaskDataService;
import com.risen.realtime.framework.service.TaskValidService;
import org.apache.commons.lang3.ObjectUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 14:06
 */
@Component
@DisallowConcurrentExecution
public class IvMDBDataPusher extends DataJobManager {

    public static final String TASK_KEY = "risen:data:iv:mdb";


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        if (TaskValidService.taskConfigValid(taskKey())) {
            return;
        }

        String startLog = "开始采集MDB文件";
        String error = "没有配置需要读取的mdb文件表名称";
        LogSender.printInfo(startLog);
        PushSourceCache pushSourceCache = SystemCacheService.getSystemObj(PushSourceCache.class);
        PushSourceEntity source = pushSourceCache.get(taskKey());
        LogUtil.info(startLog);
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(source.getDataTable())) {
            LogUtil.info(error);
            LogSender.printError(error);
        }
        TaskDataService.startMonitorFolder(taskKey(), IvMDBFileListener.class, IvMDBDirectoryFilter.class);
    }


    @Override
    public String taskKey() {
        return TASK_KEY;
    }

    @Override
    public JobTypeEnum taskType() {
        return JobTypeEnum.BUSINESS_ONCE;
    }


    @Override
    public boolean runWithServiceStart() {
        return true;
    }


}
