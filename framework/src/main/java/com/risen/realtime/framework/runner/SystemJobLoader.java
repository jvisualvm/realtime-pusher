package com.risen.realtime.framework.runner;

import com.risen.common.util.LogUtil;
import com.risen.common.util.ThreadLocalUtil;
import com.risen.realtime.framework.base.BaseTaskLoader;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.SystemObjectCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.service.SystemCacheService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 6)
@AllArgsConstructor
public class SystemJobLoader extends BaseTaskLoader implements CommandLineRunner {

    private SystemObjectCache systemObjectCache;

    @Override
    public void run(String... args) throws Exception {
        List<DataJobManager> jobList = DataJobManager.getImplTree();
        ThreadLocalUtil.inLocal.set(systemObjectCache);
        if (!CollectionUtils.isEmpty(jobList)) {
            LogUtil.info("开始添加任务,任务数量:{}", jobList.size());
            jobList.forEach(job -> {
                LogUtil.info("启动任务:{}", job.taskKey());
                PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
                PushTaskEntity pushTask = pushTaskCache.get(job.taskKey());
                if (ObjectUtils.isEmpty(pushTask) || !pushTask.getEnable()) {
                    LogUtil.info("当前业务任务被禁用或者被删除无法启动");
                    return;
                }
                if (job.taskType() == JobTypeEnum.SYSTEM) {
                    addCronStartTask(job, () -> {
                        return job.runWithServiceStart();
                    });
                } else if (job.taskType() == JobTypeEnum.BUSINESS_ONCE) {
                    addOnceStartTask(job, () -> {
                        return job.runWithServiceStart();
                    });
                } else if (job.taskType() == JobTypeEnum.BUSINESS_CRON) {
                    addCronStartTask(job, () -> {
                        return job.runWithServiceStart();
                    });
                }
            });
        }
    }

}
