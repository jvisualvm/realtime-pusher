package com.risen.realtime.framework.manager;

import com.risen.common.exception.BusinessException;
import com.risen.quartz.manager.QuartzManager;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushTaskEntity;
import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:12
 */
@Component
@AllArgsConstructor
public class JobManager {

    private QuartzManager quartzManager;
    private PushTaskCache pushTaskCache;

    public void startCronJob(String taskKey, Class<? extends Job> cls) {
        PushTaskEntity cache = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(cache) || ObjectUtils.isEmpty(cache.getCron())) {
            throw new BusinessException("没有配置该任务或未配置执行周期" + taskKey);
        }
        quartzManager.addCronJob(taskKey, cls, cache.getCron(), null);
    }


    public void startSimpleJob(String taskKey, Class<? extends Job> cls) {
        PushTaskEntity cache = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(cache) || ObjectUtils.isEmpty(cache.getCron())) {
            throw new BusinessException("没有配置该任务或未配置执行周期" + taskKey);
        }
        quartzManager.addSimpleOnceJob(taskKey, cls);
    }

    public void removeJob(String jobName) {
        quartzManager.removeJob(jobName);
    }


    public boolean taskIsRunning(String jobName) {
        return quartzManager.taskIsRunning(jobName);
    }


    public void pauseTask(String jobName) {
        quartzManager.pauseTask(jobName);
    }

    public void resumeTask(String jobName) {
        quartzManager.resumeTask(jobName);
    }


}
