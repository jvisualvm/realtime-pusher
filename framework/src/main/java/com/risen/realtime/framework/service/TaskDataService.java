package com.risen.realtime.framework.service;

import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.monitor.BaseDirectoryFilter;
import com.risen.realtime.framework.monitor.BaseFileListener;
import com.risen.realtime.framework.monitor.BaseFileMonitor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 22:04
 */
public class TaskDataService {

    public static <F extends BaseFileListener, D extends BaseDirectoryFilter> void startMonitorFolder(String taskKey, Class<F> fileListener, Class<D> directListener) {
        //开始采集IV文件
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(pushTask) || ObjectUtils.isEmpty(pushTask.getFileDirectory()) || ObjectUtils.isEmpty(pushTask.getFileType())) {
            String errorLog = "当前任务没有配置监控目录，请在管理系统配置监控目录";
            LogUtil.error(errorLog);
        }
        F ivFileListener = SystemCacheService.getSystemObj(fileListener);
        D csvDirectoryFilter = SystemCacheService.getSystemObj(directListener);
        BaseFileMonitor.monitor(taskKey, pushTaskCache.get(taskKey).getFileDirectory(), pushTask.getFileType(), ivFileListener, csvDirectoryFilter, TimeUnit.SECONDS.toMillis(1));
    }


    public static void updateMonitor(PushTaskEntity newTask, PushTaskEntity oldTask) {
        if (!JobTypeEnum.SYSTEM.getType().equals(oldTask.getTaskType())) {
            //禁用
            if (!newTask.getEnable() && oldTask.getEnable()) {
                BaseFileMonitor.delete(oldTask.getTaskKey());
            }
            //开启
            if ((newTask.getEnable() && !oldTask.getEnable()) || modifyFileDirectory(newTask, oldTask)) {
                if (!contain(oldTask.getTaskKey())) {
                    TaskDataService.startMonitorFolder(oldTask.getTaskKey(), BaseFileListener.getImplTree(oldTask.getTaskKey()).getClass(), BaseDirectoryFilter.getImplTree(oldTask.getTaskKey()).getClass());
                } else {
                    BaseFileMonitor.delete(oldTask.getTaskKey());
                    TaskDataService.startMonitorFolder(oldTask.getTaskKey(), BaseFileListener.getImplTree(oldTask.getTaskKey()).getClass(), BaseDirectoryFilter.getImplTree(oldTask.getTaskKey()).getClass());
                }
            }
        }
    }


    private static boolean modifyFileDirectory(PushTaskEntity newTask, PushTaskEntity oldTask) {
        return PredicateUtil.isNotEmpty(newTask.getFileDirectory()) && !newTask.getFileDirectory().equals(oldTask.getFileDirectory());
    }

    public static boolean contain(String taskKey) {
        return BaseFileMonitor.contain(taskKey);
    }


}
