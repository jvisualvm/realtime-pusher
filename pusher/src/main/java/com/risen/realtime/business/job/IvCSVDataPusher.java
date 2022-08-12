package com.risen.realtime.business.job;

import com.risen.common.util.LogUtil;
import com.risen.realtime.business.filter.IvCsvDirectoryFilter;
import com.risen.realtime.business.listener.IvCSVFileListener;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.service.TaskDataService;
import com.risen.realtime.framework.service.TaskValidService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 10:11
 */
@Component
@DisallowConcurrentExecution
public class IvCSVDataPusher extends DataJobManager {

    public static final String TASK_KEY = "risen:data:iv:csv";


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            if (TaskValidService.taskConfigValid(taskKey())) {
                return;
            }
            String startLog = "开始采集IV文件";
            LogSender.printInfo(startLog);
            LogUtil.info(startLog);
            TaskDataService.startMonitorFolder(taskKey(), IvCSVFileListener.class, IvCsvDirectoryFilter.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return TASK_KEY;
    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }

    @Override
    public JobTypeEnum taskType() {
        return JobTypeEnum.BUSINESS_ONCE;
    }

}
