package com.risen.realtime.framework.base;

import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.manager.JobManager;
import com.risen.realtime.framework.service.SystemCacheService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/20 15:38
 */
@Component
public abstract class BaseTaskLoader {

    public void addCronStartTask(DataJobManager item, Supplier<Boolean> isStart) {
        JobManager jobManager = SystemCacheService.getSystemObj(JobManager.class);
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(item.taskKey());
        if (ObjectUtils.isEmpty(pushTask)) {
            LogUtil.error("任务:{}配置信息已经丢失，无法启动", item.taskKey());
            return;
        }
        if (isStart.get()) {
            jobManager.removeJob(item.taskKey());
            jobManager.startCronJob(item.taskKey(), item.getClass());
            if (!pushTask.getEnable()) {
                jobManager.pauseTask(item.taskKey());
            }
        }
    }


    public void addOnceStartTask(DataJobManager item, Supplier<Boolean> isStart) {
        JobManager jobManager = SystemCacheService.getSystemObj(JobManager.class);
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(item.taskKey());
        if (ObjectUtils.isEmpty(pushTask)) {
            LogUtil.error("任务:{}配置信息已经丢失，无法启动", item.taskKey());
            return;
        }
        if (isStart.get()) {
            jobManager.removeJob(item.taskKey());
            jobManager.startSimpleJob(item.taskKey(), item.getClass());
            if (!pushTask.getEnable()) {
                jobManager.pauseTask(item.taskKey());
            }
        }
    }
}
