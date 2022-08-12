package com.risen.realtime.framework.service;

import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 18:32
 */
public class TaskValidService {

    public static boolean taskConfigValid(String taskKey) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(pushTask) || !pushTask.getEnable()) {
            String errorLog = "当前任务已经被禁用,无法采集数据，请前往管理页面启用";
            LogUtil.error(errorLog);
            return true;
        }

        if (ObjectUtils.isEmpty(pushTask) || ObjectUtils.isEmpty(pushTask.getFileDirectory()) || ObjectUtils.isEmpty(pushTask.getFileType())) {
            String errorLog = "当前任务没有配置监控目录，请在管理系统配置监控目录";
            LogUtil.info(errorLog);
            return true;
        }

        PushSinkEntity pushSink = pushTaskCache.getTaskSink(taskKey);
        if (ObjectUtils.isEmpty(pushSink) || !pushSink.getEnable()) {
            String errorLog = "当前数据源已经被禁用,无法采集数据，请前往管理页面启用";
            LogUtil.error(errorLog);
            return true;
        }

        if (ObjectUtils.isEmpty(pushSink)
                || ObjectUtils.isEmpty(pushSink.getUrl())
                || ObjectUtils.isEmpty(pushSink.getDataTable())
                || ObjectUtils.isEmpty(pushSink.getField())
                || ObjectUtils.isEmpty(pushSink.getDataMode())) {
            String errorLog = "当前任务没有配置数据源，数据模式、或者缺失配置，无法执行，请配置数据源";
            LogUtil.error(errorLog);
            return true;
        }
        return false;
    }


}
